/*
 * This enum is used to store the paths to the test files.
 */
public enum Distributer {
    // Theme test files (Week 1.)
    test("src\\test\\test.txt"),
    t1_1a("data\\T0.1\\t1-1a.txt"),
    t1_1b("data\\T0.1\\t1-1b.txt"),
    t1_1c("data\\T0.1\\t1-1c.txt"),
    t1_1d("data\\T0.1\\t1-1d.txt"),
    t1_2a("data\\T0.1\\t1-2a.txt"),
    t1_2b("data\\T0.1\\t1-2b.txt"),
    t1_2cde("data\\T0.1\\t1-2cde.txt"),
    t1_2fg("data\\T0.1\\t1-2fg.txt"),
    t1_3a("data\\T0.1\\t1-3a.txt"),
    t1_3b("data\\T0.1\\t1-3b.txt"),
    tf1_1("data\\T0.1\\tf1-1.txt"),
    // Theme test files (Week 2.)
    t2_1ab("data\\T0.2\\t2-1ab.txt"),
    t2_1c("data\\T0.2\\t2-1c.txt"),
    t2_2a("data\\T0.2\\t2-2a.txt"),
    t2_3a("data\\T0.2\\t2-3a.txt"),
    t2_4a("data\\T0.2\\t2-4a.txt"),
    t2_5a("data\\T0.2\\t2-5a.txt"),
    t2_5b("data\\T0.2\\t2-5b.txt"),
    t2_5c("data\\T0.2\\t2-5c.txt"),
    t2_6a("data\\T0.2\\t2-6a.txt"),
    t2_7a("data\\T0.2\\t2-7a.txt"),
    t2_8a("data\\T0.2\\t2-8a.txt"),
    tf2_1("data\\T0.2\\tf2-1.txt"),
    tf2_2("data\\T0.2\\tf2-2.txt"); 

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

