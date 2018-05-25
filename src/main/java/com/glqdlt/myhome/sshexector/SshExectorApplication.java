package com.glqdlt.myhome.sshexector;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

//ref
//https://www.programcreek.com/java-api-examples/?api=com.jcraft.jsch.UserInfo

@Slf4j
@SpringBootApplication
public class SshExectorApplication implements CommandLineRunner {

    @Value("${guest.network.ip}")
    String IP;

    @Value("${guest.ssh.port}")
    Integer PORT;

    @Value("${guest.user.id}")
    String ID;

    @Value("${guest.user.pw}")
    String PW;


    public static void main(String[] args) {
        SpringApplication.run(SshExectorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        JSch jSch = new JSch();

        Session session = jSch.getSession(ID, IP, PORT);

        UserInfo info = new UserInfo() {
            @Override
            public String getPassphrase() {
                return PW;
            }

            @Override
            public String getPassword() {
                return PW;
            }

            @Override
            public boolean promptPassword(String s) {
                return true;
            }

            @Override
            public boolean promptPassphrase(String s) {
                return true;
            }

            @Override
            public boolean promptYesNo(String s) {
                return true;
            }

            @Override
            public void showMessage(String s) {

                log.info(s);

            }
        };

        session.setUserInfo(info);
        session.connect();


        String cmd = "sudo shutdown -P 00";
        Channel channel = session.openChannel("exec");

        ((ChannelExec) channel).setCommand(cmd);

        try (InputStreamReader inputStreamReader = new InputStreamReader(channel.getInputStream())) {

//            TODO write body

        }
        ;

        channel.connect();


        session.disconnect();

    }
}
