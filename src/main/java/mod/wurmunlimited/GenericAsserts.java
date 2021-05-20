package mod.wurmunlimited;

import com.wurmonline.server.creatures.Creature;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class GenericAsserts {
    public static class NearlyEquals extends TypeSafeMatcher<Long> {

        private final Long first;
        private Long second;
        private final long threshold;

        private NearlyEquals(Long first, long threshold) {
            this.first = first;
            this.threshold = threshold;
        }

        @Override
        protected boolean matchesSafely(Long second) {
            this.second = second;

            return first.equals(second) ||
                           (second <= first + threshold && second >= first - threshold) ||
                           (first <= second + threshold && first >= second - threshold);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(" " + first + " and " + second + " to be nearly equal.");
        }

        @Override
        public void describeMismatchSafely(Long other, Description description) {
            description.appendText(" but they were not within " + threshold + ".");
        }
    }

    public static Matcher<Long> nearlyEquals(Long first) {
        return new NearlyEquals(first, 1);
    }

    public static Matcher<Long> nearlyEquals(Long first, long threshold) {
        return new NearlyEquals(first, threshold);
    }
}
