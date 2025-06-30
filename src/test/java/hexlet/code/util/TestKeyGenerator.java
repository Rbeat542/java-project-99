package hexlet.code.util;

import org.junit.jupiter.api.BeforeAll;
import java.io.File;
import java.io.IOException;

public class TestKeyGenerator {

    @BeforeAll
    public static void generateKeys() throws IOException, InterruptedException {
        File certsDir = new File("src/test/resources/certs");
        if (!certsDir.exists()) {
            certsDir.mkdirs();
        }

        File privateKey = new File(certsDir, "private.pem");
        File publicKey = new File(certsDir, "public.pem");

        if (privateKey.exists() && publicKey.exists()) {
            return; // Уже сгенерированы
        }

        ProcessBuilder genPrivate = new ProcessBuilder(
                "openssl", "genpkey",
                "-out", privateKey.getAbsolutePath(),
                "-algorithm", "RSA",
                "-pkeyopt", "rsa_keygen_bits:2048"
        );
        if (genPrivate.start().waitFor() != 0) throw new RuntimeException("Ошибка генерации private.pem");

        ProcessBuilder genPublic = new ProcessBuilder(
                "openssl", "rsa",
                "-in", privateKey.getAbsolutePath(),
                "-pubout",
                "-out", publicKey.getAbsolutePath()
        );
        if (genPublic.start().waitFor() != 0) throw new RuntimeException("Ошибка генерации public.pem");
    }
}
