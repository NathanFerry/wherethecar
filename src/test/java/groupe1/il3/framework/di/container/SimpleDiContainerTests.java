package groupe1.il3.framework.di.container;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("Di Container tests")
public class SimpleDiContainerTests {

    private DependencyInjectionContainer container;

    @BeforeEach
    void init() {
        container = new SimpleDiContainer();
    }

    @ParameterizedTest
    @MethodSource("commonTypesMethodSource")
    <T> void registerCommonTypesTest(Class<T> type, T instance) {
        assertDoesNotThrow(() -> container.register(type, instance));
    }

    @ParameterizedTest
    @MethodSource("commonTypesMethodSource")
    <T> void resolveCommonTypesTest(Class<T> type, T instance) {
        container.register(type, instance);
        T resolved = assertDoesNotThrow(() -> container.resolve(type));

        assertEquals(instance, resolved);
    }

    @Test
    void registerNullTest() {
        assertDoesNotThrow(() -> container.register(Integer.class, null));
    }

    @Test
    void resolveNullTest() {
        container.register(Integer.class, null);
        assertThrows(IllegalArgumentException.class, () -> container.resolve(Integer.class));
    }

    @Test
    void resolveNotSetTest() {
        assertThrows(IllegalArgumentException.class, () -> container.resolve(Integer.class));
    }

    static Stream<Arguments> commonTypesMethodSource() {
        return Stream.of(
                arguments(String.class, "test"),
                arguments(Integer.class, 42),
                arguments(Double.class, 3.14),
                arguments(Boolean.class, true)
        );
    }
}
