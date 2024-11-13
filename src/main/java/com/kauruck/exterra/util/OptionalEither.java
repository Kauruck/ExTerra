package com.kauruck.exterra.util;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class OptionalEither<R, L> {

    private static final OptionalEither<?, ?> EMPTY = new OptionalEither<>(null, null);

    private final R right;
    private final L left;

    private OptionalEither(R right, L left) {
        this.right = right;
        this.left = left;
    }

    public static <R, L> OptionalEither<R, L> left(L left) {
        return new OptionalEither<>(null, Objects.requireNonNull(left));
    }

    public static <R, L> OptionalEither<R, L> right(R right) {
        return new OptionalEither<>(Objects.requireNonNull(right), null);
    }

    public static <R, L> OptionalEither<R, L> leftNullable(L left) {
        if (left == null) {
            return empty();
        }
        return new OptionalEither<>(null, left);
    }

    public static <R, L> OptionalEither<R, L> rightNullable(R right) {
        if (right == null) {
            return empty();
        }
        return new OptionalEither<>(right, null);
    }

    public static <R, L> OptionalEither<R, L> empty() {
        @SuppressWarnings("unchecked")
        OptionalEither<R, L> ret = (OptionalEither<R, L>) EMPTY;
        return ret;
    }

    public R getRight() {
        if (right == null) {
            throw new NoSuchElementException("No right value present");
        }
        return right;
    }

    public L getLeft() {
        if (left == null) {
            throw new NoSuchElementException("No right value present");
        }
        return left;
    }

    public boolean isPresent() {
        return left != null || right != null;
    }

    public boolean isLeftPresent() {
        return left != null;
    }

    public boolean isRightPresent() {
        return right != null;
    }

    public boolean isEmpty() {
        return left == null && right == null;
    }

    public boolean isLeftEmpty() {
        return left == null;
    }

    public boolean isRightEmpty() {
        return left == null;
    }

    public void ifLeftPresent(Consumer<L> consumer) {
        if (left != null) {
            consumer.accept(left);
        }
    }

    public void ifRightPresent(Consumer<R> consumer) {
        if (right != null) {
            consumer.accept(right);
        }
    }

    public void ifLeftPresentOrElse(Consumer<L> consumer, Runnable empty) {
        if (left != null) {
            consumer.accept(left);
        } else {
            empty.run();
        }
    }

    public void ifRightPresentOrElse(Consumer<R> consumer, Runnable empty) {
        if (right != null) {
            consumer.accept(right);
        } else {
            empty.run();
        }
    }

    public OptionalEither<R, L> filterRight(Predicate<? super R> predicate) {
        Objects.requireNonNull(predicate);
        if (isRightEmpty()) {
            return this;
        } else {
            return predicate.test(right) ? this : empty();
        }
    }

    public OptionalEither<R, L> filterLeft(Predicate<? super L> predicate) {
        Objects.requireNonNull(predicate);
        if (isLeftEmpty()) {
            return this;
        } else {
            return predicate.test(left) ? this : empty();
        }
    }

    public OptionalEither<R, L> filter(Predicate<? super L> predicateLeft, Predicate<? super R> predicateRight) {
        return filterLeft(predicateLeft).filterRight(predicateRight);
    }

    public <U> OptionalEither<R, U> mapLeft(Function<? super  L, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (isLeftEmpty()) {
            return OptionalEither.rightNullable(right);
        } else {
            return OptionalEither.leftNullable(mapper.apply(left));
        }
    }

    public <U> OptionalEither<U, L> mapRight(Function<? super  R, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (isRightEmpty()) {
            return OptionalEither.leftNullable(left);
        } else {
            return OptionalEither.rightNullable(mapper.apply(right));
        }
    }

    public <U, O> OptionalEither<U, O> map(Function<? super  L, ? extends O> mapperLeft,
                                           Function<? super  R, ? extends U> mapperRight) {
        OptionalEither<R, O> mappedLeft = this.mapLeft(mapperLeft);
        return mappedLeft.mapRight(mapperRight);
    }

    public <U> OptionalEither<R, U> flatMapLeft(Function<? super  L, Optional<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        if (isLeftEmpty()) {
            return OptionalEither.rightNullable(right);
        } else {
            return OptionalEither.leftNullable(mapper.apply(left).orElse(null));
        }
    }

    public <U> OptionalEither<U, L> flatMapRight(Function<? super  R, Optional<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        if (isRightEmpty()) {
            return OptionalEither.leftNullable(left);
        } else {
            return OptionalEither.rightNullable(mapper.apply(right).orElse(null));
        }
    }

    public <U, O> OptionalEither<U, O> flatMap(Function<? super  L, Optional<? extends O>> mapperLeft,
                                           Function<? super  R, Optional<? extends U>> mapperRight) {
        OptionalEither<R, O> mappedLeft = this.flatMapLeft(mapperLeft);
        return mappedLeft.flatMapRight(mapperRight);
    }

    public OptionalEither<R, L> or(Supplier<? extends OptionalEither<R, L>> supplier) {
        if (this.isPresent()) {
            return this;
        } else {
            return supplier.get();
        }
    }

    public Stream<L> streamLeft() {
        if (this.isLeftEmpty()) {
            return Stream.empty();
        } else {
            return Stream.of(this.left);
        }
    }

    public Stream<R> streamRight() {
        if (this.isLeftEmpty()) {
            return Stream.empty();
        } else {
            return Stream.of(this.right);
        }
    }

    public L leftOrElse(L other) {
        return isLeftPresent() ? left : other;
    }

    public R rightOrElse(R other) {
        return isRightPresent() ? right : other;
    }

    public L leftOrElseGet(Supplier<? extends L> other) {
        return isLeftPresent() ? left : other.get();
    }

    public R rightOrElseGet(Supplier<? extends R> other) {
        return isRightPresent() ? right : other.get();
    }

    public <X extends Throwable> L leftOrElseThrow(Supplier<? extends X> supplier) throws X {
        if (isLeftPresent()) {
            return left;
        } else {
            throw supplier.get();
        }
    }


    public L leftOrElseThrow() throws NoSuchElementException{
        return leftOrElseThrow(() -> new NoSuchElementException("Left was empty"));
    }

    public <X extends Throwable> R rightOrElseThrow(Supplier<? extends X> supplier) throws X {
        if (isRightPresent()) {
            return right;
        } else {
            throw supplier.get();
        }
    }

    public R rightOrElseThrow() throws NoSuchElementException{
        return rightOrElseThrow(() -> new NoSuchElementException("Right was empty"));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        return obj instanceof OptionalEither<?, ?> other
                && Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(right);
        result = 31 * result + Objects.hashCode(left);
        return result;
    }

    @Override
    public String toString() {
        return "OptionalEither{" +
                "right=" + right +
                ", left=" + left +
                '}';
    }
}
