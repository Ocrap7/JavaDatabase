public class test {
    public static void main(String[] args) throws Exception {
        DatabaseManager db = new DatabaseManager();

        Repository<User> userRepo = db.getRepository(User.class);

        User user = new User();
        user.id = 5;
        user.name = "potato";
        user.age = 16;

        User user1 = new User();
        user1.id = 6;
        user1.name = "Oliver";
        user1.age = 16;

        userRepo.save(user);
        // userRepo.save(user1);

        User user2 = new User();
        user2.age = 16;

        for (User u : userRepo.find(user2, "age", "name")) {
            System.out.println(u);
        }
    }
}