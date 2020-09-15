package tools;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class IdGen {

    @Test
    public void genIds() {
        for (int i = 0; i < 10; i++) {
            String guid = UUID.randomUUID().toString();
            guid = StringUtils.remove(guid,'-');
            System.err.println(guid);
        }
    }
}
