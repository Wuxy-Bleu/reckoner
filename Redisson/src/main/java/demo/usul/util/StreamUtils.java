package demo.usul.util;

import java.util.stream.Stream;

public class StreamUtils {

    public<T> Stream<T> appendToStream(Stream<? extends T> stream, T element) {
        return Stream.concat(stream, Stream.of(element));
    }
}
