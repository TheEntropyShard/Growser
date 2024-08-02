import me.theentropyshard.growser.gemini.client.GeminiRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeminiRequestTest {
    @Test
    void test_scheme_is_not_gemini() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new GeminiRequest("geminiprotocol.net"));
        assertEquals(e.getMessage(), "URI scheme for Gemini must be 'gemini://'");
    }

    @Test
    void test_exception_if_userinfo_is_present() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new GeminiRequest("gemini://user:pass@geminiprotocol.net"));
        assertEquals(e.getMessage(), "User info must not be present in Gemini URI");
    }
}
