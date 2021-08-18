package chapter02;

/**
 * @author: jinyangrao on 2021/8/18
 * @description:
 */
public class StopThreadUnsafe {
    public static class User {
        private int id;
        private String name;

        public User() {
            this.id = 0;
            this.name = "0";
        }

        void setId(int id) {
            this.id = id;
        }

        int getId() {
            return this.id;
        }

        void setName(String name) {
            this.name = name;
        }

        String getName() {
            return this.name;
        }

        @Override
        public String toString() {
            return "[" + "id=" + id + ", name='" + name + '\'' + ']';
        }
    }

    private static User user = new User();

    public static class ChangeObjectThread extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (user) {
                    int v = (int) (System.currentTimeMillis() / 1000);
                    user.setId(v);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    user.setName(String.valueOf(v));
                }
                Thread.yield();
            }
        }
    }

    public static class ReadObjectThread extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (user) {
                    if (user.getId() != Integer.parseInt(user.getName())) {
                        System.out.println(user);
                    }
                }
                Thread.yield();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ReadObjectThread().start();
        while (true) {
            Thread t = new ChangeObjectThread();
            t.start();
            Thread.sleep(150);
            t.stop();
        }
    }
}
