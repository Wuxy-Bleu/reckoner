package demo.usul.aspect;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.redisson.api.RBloomFilterReactive;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

class BloomFilterAspectTest {

    @Mock
    RBloomFilterReactive<Object> bloomFilterMock;

    @Test
    void testMonoWhenFailureStopsSubsequentOperations() {
//        // Arrange
//        String keyName = "testKey";
//        List<Object> ids = List.of("id1", "id2");
//
//        // Mock bloomFilter methods
//        when(bloomFilterMock.delete()).thenReturn(Mono.error(new RuntimeException("Delete failed")));
//        when(bloomFilterMock.tryInit(any(), any())).thenReturn(Mono.just(true)); // won't be executed
//        when(bloomFilterMock.add(any())).thenReturn(Mono.just(1)); // won't be executed
//        when(bloomFilterMock.expire(any())).thenReturn(Mono.just(true)); // won't be executed
//
//        // Act
//        Mono<Void> result = myService.cacheAccountsReactive(ids, 1000L);
//
//        // Assert
//        // Verify that the delete method was called, but others should not be called due to the error
//        StepVerifier.create(result)
//                .expectError(RuntimeException.class)
//                .verify();
//
//        // Verify that the methods after the failure were not invoked
//        verify(bloomFilterMock).delete();
//        verify(bloomFilterMock, never()).tryInit(any(), any());
//        verify(bloomFilterMock, never()).add(any());
//        verify(bloomFilterMock, never()).expire(any());
    }

}