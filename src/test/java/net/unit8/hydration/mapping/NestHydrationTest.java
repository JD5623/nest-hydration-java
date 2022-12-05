package net.unit8.hydration.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.unit8.hydration.NestHydration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NestHydrationTest {
    private static final Logger LOG = LoggerFactory.getLogger(NestHydrationTest.class);
    ObjectMapper mapper = new ObjectMapper();
    NestHydration nestHydration;

    @BeforeEach
    void setup() {
        nestHydration = new NestHydration();
    }

    @Test
    void nullData() {
        assertThat(nestHydration.nest(null, null))
                .isNull();
    }

    @Test
    void emptyData() {
        assertThat(nestHydration.nest(List.of())).isNull();
    }

    @Test
    void hintedMappingIdTypeUnspecifiedFirstColumnShouldBeUsedAsId() throws IOException {
        Object nest = nestHydration.nest(java.util.Arrays.asList(
                new LinkedHashMap<String,Object>() {
                    {
                        put("_a", 1);
                        put("_b__c", "c1");
                    }
                },
                new LinkedHashMap<String,Object>() {
                    {
                        put("_a", 1);
                        put("_b__c", "c2");
                    }
                },
                new LinkedHashMap<String,Object>() {
                    {
                        put("_a", 2);
                        put("_b__c", "c3");
                    }
                },
                new LinkedHashMap<String,Object>() {
                    {
                        put("_a", 2);
                        put("_b__c", "c4");
                    }
                }));
        LOG.debug(mapper.writeValueAsString(nest));
        assertThat(nest).asList().hasSize(2);
    }

    @Test
    void hintedMappingIdTypeSpecifiedFirstColumnShouldBeUsed() throws IOException {
        Object nest = nestHydration.nest(java.util.Arrays.asList(
                new LinkedHashMap<String,Object>() {
                    {
                        put("_a___ID", 1);
                        put("_b__c", "c1");
                    }
                },
                new LinkedHashMap<String,Object>() {
                    {
                        put("_a___ID", 1);
                        put("_b__c", "c2");
                    }
                },
                new LinkedHashMap<String,Object>() {
                    {
                        put("_a___ID", 2);
                        put("_b__c", "c3");
                    }
                },
                new LinkedHashMap<String,Object>() {
                    {
                        put("_a___ID", 2);
                        put("_b__c", "c4");
                    }
                }));
        LOG.debug(mapper.writeValueAsString(nest));
    }

    @Test
    void hintedMappingIdTypeSpecifiedOnOtherColumnShouldBeUsed() throws IOException {
        Object nest = nestHydration.nest(java.util.Arrays.asList(
                new LinkedHashMap<String,Object>() {
                    {
                        put("_a__c", "c1");
                        put("_b___ID", 1);
                    }
                },
                new LinkedHashMap<String,Object>() {
                    {
                        put("_a__c", "c2");
                        put("_b___ID", 1);
                    }
                },
                new LinkedHashMap<String,Object>() {
                    {
                        put("_a__c", "c3");
                        put("_b___ID", 2);
                    }
                },
                new LinkedHashMap<String,Object>() {
                    {
                        put("_a__c", "c4");
                        put("_b___ID", 2);
                    }
                }));
        LOG.debug(mapper.writeValueAsString(nest));
    }

}
