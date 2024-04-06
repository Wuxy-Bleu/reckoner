//package demo.usul.util;
//
//public class LambdaUtils {
//     private final static ExceptionFunction FUNC = t -> {
//         try{
//             return
//         }
//     }
//
//    private static <T, R> R wrapException(ExceptionFunction<T, R> function) {
//        return t -> {
//            try {
//                return function.apply(t);
//            } catch (Exception e) {
//                throw new RuntimeException("Error processing JSON", e);
//            }
//        };
//    }
//
//    @FunctionalInterface
//    private interface ExceptionFunction<T, R> {
//
//        R apply(T t) throws Exception;
//    }
//}
//
