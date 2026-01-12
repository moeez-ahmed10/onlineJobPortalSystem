import java.util.*;

class Candidate {
    private String userId;
    private String name;
    private List<String> skills;
    private int experience;
    private String location;

    Candidate(String userId, String name, List<String> skills, int experience, String location) {
        this.userId = userId;
        this.name = name;
        this.skills = skills;
        this.experience = experience;
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public List<String> getSkills() {
        return skills;
    }

    public int getExperience() {
        return experience;
    }

    public String getLocation() {
        return location;
    }

}

class Job {
    private String jobId;
    private String title;
    private List<String> jobSkills;
    private int minExperience;
    private String location;

    Job(String jobId, String title, List<String> skills, int exp, String location) {
        this.jobId = jobId;
        this.title = title;
        this.jobSkills = skills;
        this.minExperience = exp;
        this.location = location;
    }

    public String getJobId() {
        return jobId;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getJobSkills() {
        return jobSkills;
    }

    public int getMinExperience() {
        return minExperience;
    }

    public String getLocation() {
        return location;
    }

    public int matchScore(Candidate candidate) {
        int score = 0;
        for (String skills : getJobSkills()) {
            if (candidate.getSkills().contains(skills)) {
                score += 10;
            }
        }
        if (candidate.getExperience() >= getMinExperience()) {
            score += 5;
        }
        if (candidate.getLocation().equalsIgnoreCase(getLocation())) {
            score += 5;
        }
        return score;
    }

}

class Application {
    private String candidateName;
    private String jobTitle;

    public Application(String candidateName, String jobTitle) {
        this.candidateName = candidateName;
        this.jobTitle = jobTitle;
    }

    public String toString() {
        return "Application from Mr/Ms." + candidateName + " Applied to " + jobTitle;
    }
}

class Scheduler {
    Queue<String> slots = new LinkedList<>();

    public void scheduleCandidate(String candidateId) {
        slots.offer(candidateId);
    }

    public void displaySchedule() {
        System.out.println("Upcoming Interviews: " + slots);
    }

}

class JobPortalSystem {
    private HashMap<String, Candidate> candidates = new HashMap<>();
    private HashMap<String, Job> jobs = new HashMap<>();
    private LinkedList<Application> logs = new LinkedList<>();
    private Scheduler scheduler = new Scheduler();
    Scanner obj = new Scanner(System.in);

    public void postJob() {
        String jobId,title,location;
        int experience;
        List<String> skills;

        System.out.print("Enter Job ID: ");
        jobId = obj.next();
        System.out.print("Enter Job Name: ");
        obj.nextLine();
        title = obj.nextLine();
        System.out.print("Enter Skills (separate by comma ( , )): ");
        skills = Arrays.asList(obj.nextLine().split(","));
        System.out.print("Enter Min Experience (years): ");
        experience = obj.nextInt();
        System.out.print("Enter Location (Office): ");
        obj.nextLine();
        location = obj.nextLine();

        Job job = new Job(jobId, title, skills, experience, location);
        jobs.put(jobId, job);
        System.out.println("Job posted successfully!");
    }

    public void registerCandidate() {
        String Id,name,location;
        int experience;
        List<String> skills;

        System.out.print("Enter Candidate ID: ");
        Id = obj.next();
        System.out.print("Enter Candidate Name: ");
        obj.nextLine();
        name = obj.nextLine();
        System.out.print("Enter Skills (separate by comma ( , )): ");
        skills = Arrays.asList(obj.nextLine().split(","));
        System.out.print("Enter Candidate Experience (years): ");
        experience = obj.nextInt();
        System.out.print("Enter Location: ");
        obj.nextLine();
        location = obj.nextLine();

        candidates.put(Id, new Candidate(Id, name, skills, experience, location));
        System.out.println("Candidate registered successfully!");
    }

    public void applyJob() {
        String candidateId,jobID;

        System.out.print("Enter Candidate ID: ");
        candidateId = obj.nextLine();
        obj.nextLine();

        System.out.print("Enter Job ID: ");
        jobID = obj.nextLine();

        if (!candidates.containsKey(candidateId)) {
            System.out.println("Invalid candidate ID.");
            return;
        }
        if (!jobs.containsKey(jobID)) {
            System.out.println("Invalid job ID.");
            return;
        }

        String candidateName = candidates.get(candidateId).getName();
        String jobTitle = jobs.get(jobID).getTitle();

        logs.add(new Application(candidateName, jobTitle));
        System.out.println("Application submitted.");
    }


    public void shortlistCandidates() {
        System.out.print("Enter Job ID to shortlist: ");
        String jobId = obj.next();
        if (!jobs.containsKey(jobId)) {
            System.out.println("Job not found.");
            return;
        }
        Job job = jobs.get(jobId);
        PriorityQueue<Candidate> ranking = new PriorityQueue<>((a, b) ->  Integer.compare(job.matchScore(b),job.matchScore(a)));
        for (Candidate candidate : candidates.values()) {
            ranking.offer(candidate);
        }
        System.out.println("Top Candidates for " + job.getTitle() + ":");
        for (int i = 0; i < 10 && !ranking.isEmpty(); i++) {
            Candidate highPriority = ranking.poll();
            System.out.println((i + 1) + ". " + highPriority.getName());
            scheduler.scheduleCandidate(highPriority.getUserId());
        }
    }

    public void viewApplications() {
        System.out.println("Application:");
        for (Application application : logs) {
            System.out.println(application);
        }
    }

    public void viewSchedule() {
        scheduler.displaySchedule();
    }

}

class jobPortal {
    public static void main(String[] args) {
        JobPortalSystem portal = new JobPortalSystem();
        Scanner obj = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\n--- Talent Forge Dashboard ---");
            System.out.println("1. Post Job");
            System.out.println("2. Register Candidate");
            System.out.println("3. Apply for Job");
            System.out.println("4. Shortlist Candidates");
            System.out.println("5. View Application Logs");
            System.out.println("6. View Interview Schedule");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = obj.nextInt();

            if (choice == 1) {
                portal.postJob();
            }
            else if (choice == 2) {
                portal.registerCandidate();
            }
            else if (choice == 3) {
                portal.applyJob();
            }
            else if (choice == 4) {
                portal.shortlistCandidates();
            }
            else if (choice == 5) {
                portal.viewApplications();
            }
            else if (choice == 6) {
                portal.viewSchedule();
            }
            else if (choice == 0) {
                System.out.println("Exiting Menu...");
                break;
            }
            else {
                System.out.println("Invalid choice !!!!!. \nPlease select between given choices.");
            }
        }
        obj.close();
    }
}
