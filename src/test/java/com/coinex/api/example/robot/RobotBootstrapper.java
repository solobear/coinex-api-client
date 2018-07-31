package com.coinex.api.example.robot;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RobotBootstrapper {

    private static final Logger logger = LoggerFactory.getLogger(RobotBootstrapper.class);
    private ExecutorService robotRunnerService = Executors.newCachedThreadPool();

    private void start() {
        List<OrderRobot> robots = null;
        try {
            robots = loadRobots();
        } catch (IOException e) {
            logger.error("load robots error", e);
        }
        for (OrderRobot robot : robots) {
            robotRunnerService.submit(new RobotRunner(robot));
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> robotRunnerService.shutdown()));
    }

    class RobotRunner implements Runnable {
        private OrderRobot robot;

        public RobotRunner(OrderRobot robot) {
            this.robot = robot;
        }

        @Override
        public void run() {
            while (true) {
                if (robotRunnerService.isShutdown() || robotRunnerService.isTerminated()) {
                    break;
                }
                int randomInt = RandomUtils.nextInt();
                if (randomInt % 2 == 0) {
                    robot.buy();
                } else {
                    robot.sell();
                }
            }
        }
    }

    private List<OrderRobot> loadRobots() throws IOException {
        List<OrderRobot> robots = new ArrayList<>();
        Properties robotConf = new Properties();
        robotConf.load(getClass().getResourceAsStream("/robots.properties"));
        long robotSize = robotConf.stringPropertyNames()
                .stream()
                .filter(s -> s.contains("accessKey"))
                .count();
        for (int i = 0; i < robotSize; i++) {
            int robotNum = i + 1;
            String accessKey = robotConf.getProperty(String.format("robot%s.accessKey", robotNum));
            String secretKey = robotConf.getProperty(String.format("robot%s.secretKey", robotNum));
            if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
                robots.add(new OrderRobot(accessKey, secretKey));
            }
        }
        return robots;
    }

    public static void main(String[] args) {
        new RobotBootstrapper().start();
    }
}
