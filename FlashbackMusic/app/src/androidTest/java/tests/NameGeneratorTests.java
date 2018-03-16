package tests;

import com.example.cse110.flashbackmusic.User;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

import org.junit.Test;

/**
 * Created by CubicDolphin on 3/14/18.
 */

public class NameGeneratorTests {
    @Test
    public void testAlex () {
        String name = User.nameGenerator("a7wolf");
        assertEquals("AdorableBestie8514", name);
    }

    @Test
    public void testAthena () {
        String name = User.nameGenerator("ahosek");
        assertEquals("AmazingPuppy7204", name);
    }

    @Test
    public void testSherry () {
        String name = User.nameGenerator("yuw340");
        assertEquals("WonderfulSparkle9616", name);
    }

    @Test
    public void testCortez () {
        String name = User.nameGenerator("cvpage");
        assertEquals("CuteHug6532", name);
    }

    @Test
    public void testStacy () {
        String name = User.nameGenerator("yic222");
        assertEquals("FluffyPuppy3335", name);
    }

    @Test
    public void testHeero () {
        String name = User.nameGenerator("hrc001");
        assertEquals("FantasticFriend2811", name);
    }

    @Test
    public void testAnastasia () {
        String name = User.nameGenerator("aglucas");
        assertEquals("CuddlySparkle6944", name);
    }

    @Test
    public void testNull () {
        String name = User.nameGenerator("");
        assertEquals("Nully", name);
    }

    @Test
    public void testDifferent () {
        String name1 = User.nameGenerator("Name1");
        String name2 = User.nameGenerator("Name2");
        String name1Again = User.nameGenerator("Name1");
        assertEquals(name1, name1Again);
        assertNotSame(name1, name2);
    }

}
