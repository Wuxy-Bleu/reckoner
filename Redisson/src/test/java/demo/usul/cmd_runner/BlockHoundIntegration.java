package demo.usul.cmd_runner;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import reactor.blockhound.BlockHound;

public class BlockHoundIntegration implements TestExecutionListener {
    @Override
    public void beforeTestClass(TestContext testContext) {
        BlockHound.install();
    }
}
