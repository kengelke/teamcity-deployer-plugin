package jetbrains.buildServer.deployer.agent.ssh;

import com.jcraft.jsch.JSchException;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.deployer.common.SSHRunnerConstants;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SSHExecRunner implements AgentBuildRunner {

    private final InternalPropertiesHolder myInternalProperties;

    public SSHExecRunner(@NotNull final InternalPropertiesHolder holder) {
        myInternalProperties = holder;
    }

    @NotNull
    public BuildProcess createBuildProcess(@NotNull AgentRunningBuild runningBuild,
                                           @NotNull final BuildRunnerContext context) throws RunBuildException {

        final SSHSessionProvider provider;
        try {
            provider = new SSHSessionProvider(context, myInternalProperties);
        } catch (JSchException e) {
            throw new RunBuildException(e);
        }

        Map<String,String> parameters = context.getRunnerParameters();
        final String command = StringUtil.notNullize(parameters.get(SSHRunnerConstants.PARAM_COMMAND));
        final String pty = parameters.get(SSHRunnerConstants.PARAM_PTY);
        return new SSHExecProcessAdapter(provider, command, pty, runningBuild.getBuildLogger());
    }

    @NotNull
    public AgentBuildRunnerInfo getRunnerInfo() {
        return new SSHExecRunnerInfo();
    }


}
