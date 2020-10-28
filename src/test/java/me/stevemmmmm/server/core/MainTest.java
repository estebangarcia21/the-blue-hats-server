package me.stevemmmmm.server.core;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

@PowerMockIgnore({ "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
        "com.sun.org.apache.xalan.*" })
public class MainTest {
    @Test(expected = NullPointerException.class)
    public void testOnEnable() throws Exception {
        Main main = PowerMockito.mock(Main.class);

        main.onEnable();

        PowerMockito.when(main, "registerAll", main).thenThrow(NullPointerException.class);
    }
}
