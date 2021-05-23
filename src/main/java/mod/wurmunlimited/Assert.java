package mod.wurmunlimited;

import com.google.common.base.Joiner;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.economy.Economy;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.TradingWindow;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Assert {

    private static final Pattern passThrough = Pattern.compile("passthrough\\{id=\"id\";text=\"([\\d]+)\"}");
    private static final Pattern defaultOption = Pattern.compile("default=\"([\\d]+)\";");

    private static String removePassThrough(String bml) {
        return passThrough.matcher(bml).replaceAll("");
    }

    private static String removePassThroughAndDefault(String bml) {
        return removePassThrough(defaultOption.matcher(bml).replaceAll(""));
    }

    public static class ContainsEither<T> extends TypeSafeMatcher<Set<T>> {

        private final T one;
        private boolean answerOne;
        private final T two;
        private boolean answerTwo;

        private ContainsEither(T one, T two) {
            this.one = one;
            this.two = two;
        }

        @Override
        protected boolean matchesSafely(Set<T> set) {
            answerOne = set.contains(one);
            answerTwo = set.contains(two);
            return answerOne || answerTwo;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" Set to contain " + one + " or " + two);
        }

        @Override
        public void describeMismatchSafely(Set<T> set, Description description) {
            if (answerOne && answerTwo)
                description.appendText(" both were contained");
            else
                description.appendText(" neither were contained");
        }
    }

    public static <T> Matcher<Set<T>> containsEither(T one, T two) {
        return new Assert.ContainsEither<>(one, two);
    }

    public static abstract class ContainsCoinsOfValue<T> extends TypeSafeMatcher<T> {

        final long value;
        long answer = 0L;

        ContainsCoinsOfValue(long value) {
            this.value = value;
        }

        protected boolean matchesSafely(Collection<Item> collection) {
            for (Item item : collection) {
                if (item.isCoin())
                    answer += Economy.getValueFor(item.getTemplateId());
            }
            return answer == value;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" coins totalling a value of " + value);
        }

        @Override
        public void describeMismatchSafely(T t, Description description) {
            description.appendText(" got a total of " + answer);
        }
    }

    public static class CreatureContainsCoinsOfValue extends ContainsCoinsOfValue<Creature> {

        CreatureContainsCoinsOfValue(long value) {
            super(value);
        }

        @Override
        protected boolean matchesSafely(Creature creature) {
            return matchesSafely(creature.getInventory().getItems());
        }
    }

    public static class CollectionContainsCoinsOfValue extends ContainsCoinsOfValue<Collection<Item>> {

        CollectionContainsCoinsOfValue(long value) {
            super(value);
        }
    }

    public static class TradingWindowContainsCoinsOfValue extends ContainsCoinsOfValue<TradingWindow> {

        TradingWindowContainsCoinsOfValue(long value) {
            super(value);
        }

        @Override
        protected boolean matchesSafely(TradingWindow window) {
            return matchesSafely(Arrays.asList(window.getItems()));
        }
    }

    public static Matcher<Creature> hasCoinsOfValue(long value) {
        return new Assert.CreatureContainsCoinsOfValue(value);
    }

    public static Matcher<Collection<Item>> containsCoinsOfValue(long value) {
        return new Assert.CollectionContainsCoinsOfValue(value);
    }

    public static Matcher<TradingWindow> windowContainsCoinsOfValue(long value) {
        return new Assert.TradingWindowContainsCoinsOfValue(value);
    }



    public static class IsWearing extends TypeSafeMatcher<Creature> {

        final Item item;
        Creature creature;
        final boolean wantWearing;

        IsWearing(Item item, boolean wantWearing) {
            this.item = item;
            this.wantWearing = wantWearing;
        }

        protected boolean matchesSafely(Creature creature) {
            this.creature = creature;
            boolean result = !wantWearing;

            for (Item it : creature.getBody().getAllItems()) {
                if (it == item) {
                    result = wantWearing;
                    break;
                }
            }

            return result;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" " + creature.getName() + (wantWearing ? "" : " not") + " wearing " + item.getNameWithGenus());
        }

        @Override
        public void describeMismatchSafely(Creature creature, Description description) {
            description.appendText(" " + creature.getName() + " was" + (wantWearing ? " not" : "") + " wearing " + item.getNameWithGenus());
        }
    }

    public static Matcher<Creature> isWearing(Item item) {
        return new Assert.IsWearing(item, true);
    }

    public static Matcher<Creature> isNotWearing(Item item) {
        return new Assert.IsWearing(item, false);
    }

    public static class ContainsMessage extends TypeSafeMatcher<Creature> {

        private final String message;

        private ContainsMessage(String message) {
            this.message = message;
        }

        @Override
        protected boolean matchesSafely(Creature creature) {
            String[] messages = WurmObjectsFactory.getCurrent().getMessagesFor(creature);
            for (String msg : messages) {
                if (msg.contains(message))
                    return true;
            }
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("A message containing " + message);
        }

        @Override
        public void describeMismatchSafely(Creature creature, Description description) {
            description.appendText("No such message found.");
        }
    }

    public static Matcher<Creature> receivedMessageContaining(String message) {
        return new Assert.ContainsMessage(message);
    }

    public static class DoesNotContainMessage extends TypeSafeMatcher<Creature> {

        private final String message;

        private DoesNotContainMessage(String message) {
            this.message = message;
        }

        @Override
        protected boolean matchesSafely(Creature creature) {
            String[] messages = WurmObjectsFactory.getCurrent().getMessagesFor(creature);
            for (String msg : messages) {
                if (msg.contains(message))
                    return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" no messages containing " + message);
        }

        @Override
        public void describeMismatchSafely(Creature creature, Description description) {
            description.appendText("At least one matching message was found.");
        }
    }

    public static Matcher<Creature> didNotReceiveMessageContaining(String message) {
        return new Assert.DoesNotContainMessage(message);
    }

    public static class InAscendingOrder extends TypeSafeMatcher<List<Integer>> {

        @Override
        protected boolean matchesSafely(List<Integer> list) {
            return list.stream().allMatch(new Predicate<Integer>() {
                int last = 0;
                @Override
                public boolean test(Integer integer) {
                    boolean toReturn = integer > last;
                    ++last;
                    return toReturn;
                }
            });
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" Integers to be in ascending order,");
        }

        @Override
        public void describeMismatchSafely(List<Integer> list, Description description) {
            description.appendText(" this was the order - " + Joiner.on(", ").join(list));
        }
    }

    public static Matcher<List<Integer>> inAscendingOrder() {
        return new Assert.InAscendingOrder();
    }

    public static class ContainsNoneOf<T> extends TypeSafeMatcher<Collection<T>> {

        private final Collection<T> other;
        private Collection<T> copy;

        private ContainsNoneOf(Collection<T> other) {
            this.other = other;
        }

        @Override
        protected boolean matchesSafely(Collection<T> collection) {
            copy = new HashSet<>(other);
            copy.removeAll(collection);
            return copy.size() == other.size();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" Collection to contain no items from other collection,");
        }

        @Override
        public void describeMismatchSafely(Collection<T> collection, Description description) {
            description.appendText(" found the following matches - " + Joiner.on(", ").join(copy));
        }
    }

    public static <T> Matcher<Collection<T>> containsNoneOf(Collection<T> other) {
        return new Assert.ContainsNoneOf<>(other);
    }

    public static class ContentsEqual<T> extends TypeSafeMatcher<Collection<T>> {

        private final Collection<T> second;
        private Collection<T> difference;

        private ContentsEqual(Collection<T> other) {
            this.second = other;
        }

        @Override
        protected boolean matchesSafely(Collection<T> first) {
            HashSet<T> firstCopy = new HashSet<>(first);
            HashSet<T> secondCopy = new HashSet<>(second);
            firstCopy.removeAll(second);
            secondCopy.removeAll(first);
            difference = new HashSet<>(firstCopy);
            difference.addAll(secondCopy);

            return difference.size() == 0;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" Collection to contain no items from other collection,");
        }

        @Override
        public void describeMismatchSafely(Collection<T> collection, Description description) {
            description.appendText(" found the following extra items - " + Joiner.on(", ").join(difference));
        }
    }

    public static <T> Matcher<Collection<T>> sameContentsAs(Collection<T> other) {
        return new Assert.ContentsEqual<>(other);
    }

    public static class BmlNotEqual extends TypeSafeMatcher<Creature> {

        @Override
        protected boolean matchesSafely(Creature creature) {
            String[] bml = WurmObjectsFactory.getCurrent().getCommunicator(creature).getBml();
            assert bml.length == 2;
            bml[0] = removePassThrough(bml[0]);
            bml[1] = removePassThrough(bml[1]);
            return Stream.of(bml).distinct().count() == bml.length;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" No BML sent to match,");
        }

        @Override
        public void describeMismatchSafely(Creature creature, Description description) {
            description.appendText(" these communications were received:\n" + Joiner.on("\n").join(WurmObjectsFactory.getCurrent().getCommunicator(creature).getBml()));
        }
    }

    public static Matcher<Creature> bmlNotEqual() {
        return new Assert.BmlNotEqual();
    }

    public static class BmlEqual extends TypeSafeMatcher<Creature> {

        private String[] bml;

        @Override
        protected boolean matchesSafely(Creature creature) {
            bml = WurmObjectsFactory.getCurrent().getCommunicator(creature).getBml();
            if (bml.length != 2)
                return false;
            bml[0] = removePassThrough(bml[0]);
            bml[1] = removePassThrough(bml[1]);
            return Stream.of(bml).distinct().count() == 1;
        }

        @Override
        public void describeTo(Description description) {
            if (bml.length != 2) {
                description.appendText(" exactly two BML");
            } else {
                description.appendText(" matching BML");
            }
        }

        @Override
        public void describeMismatchSafely(Creature creature, Description description) {
            if (bml.length != 2) {
                description.appendText(" received " + bml.length);
            } else {
                description.appendText(" these communications were received:\n" + Joiner.on("\n").join(WurmObjectsFactory.getCurrent().getCommunicator(creature).getBml()));
            }
        }
    }

    public static Matcher<Creature> bmlEqual() {
        return new Assert.BmlEqual();
    }

    public static class Contains extends TypeSafeMatcher<String> {

        private final String sub;

        private Contains(String sub) {
            this.sub = sub;
        }

        @Override
        protected boolean matchesSafely(String str) {
            return str.contains(sub);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" String to contain " + sub);
        }

        @Override
        public void describeMismatchSafely(String str, Description description) {
            description.appendText(sub + " not found in " + str);
        }
    }

    public static Matcher<String> contains(String other) {
        return new Contains(other);
    }

    public static class BMLContains extends TypeSafeMatcher<Creature> {

        private final String content;
        private String lastBML;

        private BMLContains(String content) {
            this.content = content;
        }

        @Override
        protected boolean matchesSafely(Creature creature) {
            lastBML = WurmObjectsFactory.getCurrent().getCommunicator(creature).lastBmlContent;
            return lastBML.contains(content);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" BML to contain " + content);
        }

        @Override
        public void describeMismatchSafely(Creature creature, Description description) {
            description.appendText(" not found in " + lastBML);
        }
    }

    public static Matcher<Creature> receivedBMLContaining(String content) {
        return new BMLContains(content);
    }

    public static class BMLNotContains extends TypeSafeMatcher<Creature> {

        private final String content;
        private String lastBML;

        private BMLNotContains(String content) {
            this.content = content;
        }

        @Override
        protected boolean matchesSafely(Creature creature) {
            lastBML = WurmObjectsFactory.getCurrent().getCommunicator(creature).lastBmlContent;
            return !lastBML.contains(content);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" BML to not contain " + content);
        }

        @Override
        public void describeMismatchSafely(Creature creature, Description description) {
            description.appendText(" but it was found in " + lastBML);
        }
    }

    public static Matcher<Creature> didNotReceiveBMLContaining(String content) {
        return new BMLNotContains(content);
    }
}
