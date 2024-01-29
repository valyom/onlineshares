package online.dates;

import java.time.LocalDate;

public class DatesIterator {
    @FunctionalInterface
    private interface NextDate {
        LocalDate nextDate(LocalDate date);
    }

    @FunctionalInterface
    private interface DateInRange {
        boolean dateInRange(LocalDate cur);
    }

    private NextDate nextImpl;
    private DateInRange inRange;

    private LocalDate start;
    private LocalDate end;
    private LocalDate curr;

    public DatesIterator(LocalDate first, int maxDaysBack) {
        this.start = (first == null) ? LocalDate.now() : first;
        this.end = this.start.minusDays(maxDaysBack);

        this.init();
    }

    public DatesIterator(LocalDate first, LocalDate last) {
        this.start = (first == null) ? LocalDate.now() : first;
        this.end = (last == null) ? LocalDate.now() : last;

        this.init();
    }

    public LocalDate first() {
        this.curr = this.start;
        return this.curr;
    }

    public LocalDate last() {
        this.curr = this.end;
        return this.end;
    }

    public LocalDate next() {
        this.curr = this.nextImpl.nextDate(this.curr);
        return this.curr;
    }

    public LocalDate current() {
        return curr;
    }

    public boolean hasNext() {
        return this.inRange.dateInRange(this.nextImpl.nextDate(this.curr));
    }

    public boolean isInRange() {
        return this.inRange.dateInRange( this.curr );
    }

    private void init() {
        this.curr = this.start;

        this.nextImpl = this.start.isAfter(this.end) ? (d) -> d.minusDays(1) : (d) -> d.plusDays(1);
        this.inRange = this.start.isAfter(this.end)
                ? (d) -> d.isAfter(this.end) || d.isEqual(this.end)
                : (d) -> d.isBefore(this.end) || d.isEqual(this.end);

    }
}
