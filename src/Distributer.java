/*
 * This enum is used to store the paths to the test files.
 */
public enum Distributer {
    // Theme test files (Week 1.)
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
    tf1_1a("data\\T0.1\\tf1-1.txt");  
    

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

