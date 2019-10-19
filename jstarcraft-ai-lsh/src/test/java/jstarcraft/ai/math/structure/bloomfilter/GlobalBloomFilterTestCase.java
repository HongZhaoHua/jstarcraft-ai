package jstarcraft.ai.math.structure.bloomfilter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

public class GlobalBloomFilterTestCase extends BloomFilterTestCase {

    private static Redisson redisson;

    @BeforeClass
    public static void beforeClass() {
        // 注意此处的编解码器
        Codec codec = new JsonJacksonCodec();
        Config configuration = new Config();
        configuration.setCodec(codec);
        configuration.useSingleServer().setAddress("redis://127.0.0.1:6379");
        redisson = (Redisson) Redisson.create(configuration);
    }

    @AfterClass
    public static void afterClass() {
        redisson.shutdown();
    }

    @Before
    public void beforeTest() {
        RKeys keys = redisson.getKeys();
        keys.flushdb();
    }

    @After
    public void afterTest() {
        RKeys keys = redisson.getKeys();
        keys.flushdb();
    }
    
    @Override
    protected BloomFilter getBloomFilter(int elments, float probability) {
        BloomFilter bloomFilter = new GlobalBloomFilter(redisson, "bloom", elments, probability);
        return bloomFilter;
    }

}
