package com.kauruck.exterra.util;

import net.minecraft.core.NonNullList;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class StreamHelper {

    public static <T> Collector<T, NonNullList<T>, NonNullList<T>> toNonNullList() {
        return new Collector<>() {
            @Override
            public Supplier<NonNullList<T>> supplier() {
                return NonNullList::create;
            }

            @Override
            public BiConsumer<NonNullList<T>, T> accumulator() {
                return NonNullList::add;
            }

            @Override
            public BinaryOperator<NonNullList<T>> combiner() {
                return (l1, l2) -> {
                    l1.addAll(l2);
                    return l2;
                };
            }

            @Override
            public Function<NonNullList<T>, NonNullList<T>> finisher() {
                return Function.identity();
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Set.of(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED, Characteristics.CONCURRENT);
            }
        };
    }
}
