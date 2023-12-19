package main;
/*
 * This enum is used to store the paths to the test files.
 */
public enum Distributer {
    // Theme test files (Week 1.)
    test("src\\test\\test.txt"),
    t1_1a("data\\T0.1\\t1_1a.txt"),
    t1_1b("data\\T0.1\\t1_1b.txt"),
    t1_1c("data\\T0.1\\t1_1c.txt"),
    t1_1d("data\\T0.1\\t1_1d.txt"),
    t1_2a("data\\T0.1\\t1_2a.txt"),
    t1_2b("data\\T0.1\\t1_2b.txt"),
    t1_2cde("data\\T0.1\\t1_2cde.txt"),
    t1_2fg("data\\T0.1\\t1_2fg.txt"),
    t1_3a("data\\T0.1\\t1_3a.txt"),
    t1_3b("data\\T0.1\\t1_3b.txt"),
    tf1_1("data\\T0.1\\tf1_1.txt"),
    // Theme test files (Week 2.)
    t2_1ab("data\\T0.2\\t2_1ab.txt"),
    t2_1c("data\\T0.2\\t2_1c.txt"),
    t2_2a("data\\T0.2\\t2_2a.txt"),
    t2_3a("data\\T0.2\\t2_3a.txt"),
    t2_4a("data\\T0.2\\t2_4a.txt"),
    t2_5a("data\\T0.2\\t2_5a.txt"),
    t2_5b("data\\T0.2\\t2_5b.txt"),
    t2_5c("data\\T0.2\\t2_5c.txt"),
    t2_6a("data\\T0.2\\t2_6a.txt"),
    t2_7a("data\\T0.2\\t2_7a.txt"),
    t2_8a("data\\T0.2\\t2_8a.txt"),
    tf2_1("data\\T0.2\\tf2_1.txt"),
    tf2_2("data\\T0.2\\tf2_2.txt"), 
    // Theme test files (Week 3.)
    t3_1a("data\\T0.3\\t3_1a.txt"),
    t3_1b("data\\T0.3\\t3_1b.txt"),
    t3_1c("data\\T0.3\\t3_1c.txt"),
    t3_2ab("data\\T0.3\\t3_2ab.txt"),
    tf3_1a("data\\T0.3\\tf3_1a.txt"),
    tf3_2a("data\\T0.3\\tf3_2a.txt"),
    tf3_3ab("data\\T0.3\\tf3_3ab.txt");

    private String url;
 
    Distributer (String envUrl) {
        this.url = envUrl;
    }
    /*
     * Returns the path to the test file.
     */
    public String getUrl() {
        return url;
    }
}

