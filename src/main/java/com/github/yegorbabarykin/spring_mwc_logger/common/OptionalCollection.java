package com.github.yegorbabarykin.spring_mwc_logger.common;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class OptionalCollection {

    private OptionalCollection() {
    }

    public static <T> Stream<T> streamOf(Collection<T> collection) {
        return Optional.ofNullable(collection).map(Collection::stream).orElse(Stream.empty());
    }

}
