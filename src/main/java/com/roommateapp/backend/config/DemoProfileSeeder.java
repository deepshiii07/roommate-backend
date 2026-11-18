package com.roommateapp.backend.config;

import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.model.User;
import com.roommateapp.backend.repository.RoommateProfileRepository;
import com.roommateapp.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "app.demo-data.enabled", havingValue = "true")
public class DemoProfileSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoommateProfileRepository profileRepository;

    private static final String[] FIRST_NAMES = {
        "Aarav","Ananya","Ishaan","Mira","Kabir","Riya","Arjun","Aisha","Vihaan","Diya",
        "Reyansh","Sana","Aditya","Tara","Dev","Myra","Neil","Zoya","Rohan","Kiara",
        "Ayaan","Meera","Krish","Naina","Vivaan","Ira","Advait","Sara","Kian","Anika",
        "Yash","Avni","Dhruv","Aditi","Arnav","Siya","Ritvik","Noor","Samar","Ishita",
        "Manav","Aarohi","Kunal","Prisha","Om","Navya","Rudra","Jhanvi","Parth","Aanya"
    };
    private static final String[] CITIES = {
        "North 24 Parganas","North 24 Parganas","North 24 Parganas","Kolkata","Kolkata",
        "Bengaluru","Bengaluru","Hyderabad","Pune","Mumbai"
    };
    private static final String[][] AREAS = {
        {"Baranagar","Dum Dum","Sodepur","Belgharia","Khardaha","Barrackpore","Lake Town","Nagerbazar","Agarpara","Panihati"},
        {"Salt Lake","New Town","Rajarhat","Bidhannagar","Baguiati","Keshtopur","Ultadanga","Barasat","Madhyamgram","Birati"},
        {"Baranagar","Dum Dum","Belgharia","Sodepur","Lake Town","Nagerbazar","Khardaha","Agarpara","Barrackpore","Panihati"},
        {"Salt Lake","Ballygunge","Garia","Jadavpur","Tollygunge","New Town","Behala","Kasba","Park Street","Dum Dum"},
        {"New Town","Rajarhat","Salt Lake","Lake Town","Bidhannagar","Garia","Jadavpur","Kasba","Behala","Ballygunge"},
        {"Koramangala","HSR Layout","Indiranagar","Whitefield","BTM Layout","Marathahalli","Jayanagar","Bellandur","JP Nagar","Sarjapur"},
        {"HSR Layout","Koramangala","BTM Layout","Jayanagar","Indiranagar","Bellandur","Whitefield","JP Nagar","Electronic City","Marathahalli"},
        {"Madhapur","Gachibowli","Kondapur","Miyapur","Kukatpally","Jubilee Hills","Hitech City","Manikonda","Begumpet","Ameerpet"},
        {"Baner","Wakad","Hinjewadi","Kothrud","Viman Nagar","Aundh","Hadapsar","Pimple Saudagar","Kharadi","Bavdhan"},
        {"Andheri","Powai","Thane","Bandra","Goregaon","Malad","Chembur","Vikhroli","Ghatkopar","Kandivali"}
    };
    private static final String[] GENDERS = {"Female","Male","Female","Male","Other"};
    private static final String[] SMOKING = {"No","No","Occasionally","Yes","No"};
    private static final String[] DRINKING = {"No","Occasionally","No","Yes","No"};
    private static final String[] RELIGION = {"Not Religious","Religious","Prefer not to say"};
    private static final String[] SLEEP = {"Early Sleeper","Night Owl","Flexible"};
    private static final String[] ROUTINE = {"Morning","Daytime","Evening","Late Night","Irregular"};
    private static final String[] CLEANLINESS = {"Very tidy","Mostly tidy","Relaxed"};
    private static final String[] SOCIAL = {"Mostly quiet","Balanced","Outgoing"};
    private static final String[] GUESTS = {"Rarely","Sometimes","Often"};

    public DemoProfileSeeder(UserRepository userRepository, RoommateProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        int inserted = 0;
        for (int i = 0; i < FIRST_NAMES.length; i++) {
            String email = String.format("demo.roomie%02d@roomiesync.test", i + 1);
            if (userRepository.findByEmailIgnoreCase(email).isPresent()) continue;

            int cityIndex = i % CITIES.length;
            int option = i % 10;
            String gender = GENDERS[i % GENDERS.length];

            User user = new User();
            user.setName(FIRST_NAMES[i] + " " + LAST_NAMES[i % LAST_NAMES.length]);
            user.setEmail(email);
            user.setPassword("DemoRoomie123!");
            user.setPhone(String.format("+91 90000%05d", i + 1));
            user.setGender(gender);
            user.setLocation(CITIES[cityIndex]);
            user.setAcademicYear(0);
            user = userRepository.save(user);

            RoommateProfile profile = new RoommateProfile();
            profile.setUser(user);
            profile.setPreferredGender(i % 4 == 0 ? "Any" : genderPreference(i));
            profile.setPreferredLocation(AREAS[cityIndex][option]);
            profile.setLifestyleSmoking(SMOKING[i % SMOKING.length]);
            profile.setLifestyleDrinking(DRINKING[(i + 1) % DRINKING.length]);
            profile.setLifestyleReligion(RELIGION[i % RELIGION.length]);
            profile.setLifestyleSleep(SLEEP[i % SLEEP.length]);
            profile.setLifestyleRoutine(ROUTINE[i % ROUTINE.length]);
            profile.setLifestyleCleanliness(CLEANLINESS[i % CLEANLINESS.length]);
            profile.setLifestyleSocial(SOCIAL[i % SOCIAL.length]);
            profile.setLifestyleGuests(GUESTS[i % GUESTS.length]);

            profile.setSmokerPreference(i % 3 == 0 ? "Doesn't matter" : (i % 2 == 0 ? "No" : "Occasionally"));
            profile.setDrinkerPreference(i % 3 == 0 ? "Doesn't matter" : (i % 2 == 0 ? "No" : "Occasionally"));
            profile.setReligiousPreference(i % 3 == 0 ? "Doesn't matter" : RELIGION[(i + 1) % RELIGION.length]);
            profile.setBedtimePreference(i % 3 == 0 ? "Flexible" : SLEEP[(i + 1) % SLEEP.length]);
            profile.setStudyTiming(i % 3 == 0 ? "Flexible" : ROUTINE[(i + 2) % ROUTINE.length]);
            profile.setCleanlinessPreference(i % 3 == 0 ? "Flexible" : CLEANLINESS[(i + 1) % CLEANLINESS.length]);
            profile.setSocialPreference(i % 3 == 0 ? "Flexible" : SOCIAL[(i + 1) % SOCIAL.length]);
            profile.setGuestPreference(i % 3 == 0 ? "Flexible" : GUESTS[(i + 1) % GUESTS.length]);
            profileRepository.save(profile);
            inserted++;
        }
        System.out.println("RoomieSync demo seeder: added " + inserted + " of 50 sample profiles.");
    }

    private static final String[] LAST_NAMES = {
        "Sharma","Das","Kapoor","Sen","Reddy","Mehta","Khan","Iyer","Roy","Patel"
    };

    private String genderPreference(int index) {
        return switch (index % 4) {
            case 1 -> "Female";
            case 2 -> "Male";
            case 3 -> "Other";
            default -> "Any";
        };
    }
}
