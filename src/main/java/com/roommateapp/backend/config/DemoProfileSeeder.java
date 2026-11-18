package com.roommateapp.backend.config;

import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.model.User;
import com.roommateapp.backend.repository.RoommateProfileRepository;
import com.roommateapp.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "app.demo-data.enabled", havingValue = "true")
public class DemoProfileSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoommateProfileRepository profileRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Fictional personas for local testing only. Names and contact details are synthetic.
    private static final String[] NAMES = {
        "Aarav Mukherjee","Ishita Banerjee","Rohan Chatterjee","Aditi Nair","Kabir Malhotra",
        "Meera Iyer","Siddharth Rao","Ananya Ghosh","Vihaan Shah","Zoya Khan",
        "Arjun Menon","Naina Kapoor","Aditya Kulkarni","Riya Das","Neil Fernandes",
        "Sana Qureshi","Karthik Reddy","Tara Sinha","Dev Patel","Mira Thomas",
        "Reyansh Agarwal","Diya Bose","Manav Bhatia","Ayesha Ali","Dhruv Joshi",
        "Avni Mehta","Yash Verma","Prisha Roy","Krishnan Pillai","Ira Dutta",
        "Samar Khanna","Kiara D'Souza","Parth Desai","Noor Siddiqui","Ritvik Sen",
        "Jhanvi Arora","Om Prakash","Navya Rao","Ayaan Sheikh","Siya Nambiar",
        "Kunal Jain","Aarohi Paul","Rudra Sethi","Ishaan Dasgupta","Myra George",
        "Farhan Ansari","Tanvi Bansal","Abhinav Krishnan","Lavanya Reddy","Sameer Merchant"
    };

    // Five profiles per city, with realistic neighbourhoods used as preferred areas.
    private static final String[] CITIES = {
        "Kolkata","Kolkata","Kolkata","Kolkata","Kolkata",
        "Bengaluru","Bengaluru","Bengaluru","Bengaluru","Bengaluru",
        "Hyderabad","Hyderabad","Hyderabad","Hyderabad","Hyderabad",
        "Pune","Pune","Pune","Pune","Pune",
        "Mumbai","Mumbai","Mumbai","Mumbai","Mumbai",
        "Chennai","Chennai","Chennai","Chennai","Chennai",
        "Delhi","Delhi","Delhi","Delhi","Delhi",
        "Gurugram","Gurugram","Gurugram","Gurugram","Gurugram",
        "Ahmedabad","Ahmedabad","Ahmedabad","Ahmedabad","Ahmedabad",
        "Jaipur","Jaipur","Jaipur","Jaipur","Jaipur"
    };

    private static final String[] AREAS = {
        "Salt Lake","New Town","Jadavpur","Ballygunge","Dum Dum",
        "Koramangala","HSR Layout","Indiranagar","Whitefield","Jayanagar",
        "Madhapur","Gachibowli","Kondapur","Kukatpally","Jubilee Hills",
        "Baner","Wakad","Hinjewadi","Kothrud","Viman Nagar",
        "Andheri West","Powai","Bandra West","Goregaon","Chembur",
        "Adyar","Velachery","Anna Nagar","T. Nagar","Sholinganallur",
        "Dwarka","Saket","Lajpat Nagar","Rohini","Greater Kailash",
        "Sector 56","Sector 45","DLF Phase 3","Sushant Lok","Sector 57",
        "Prahlad Nagar","Satellite","Bodakdev","Vastrapur","Thaltej",
        "Malviya Nagar","Vaishali Nagar","Mansarovar","Jagatpura","C-Scheme"
    };

    private static final String[] GENDERS = {
        "Female","Female","Male","Female","Male","Other","Female","Male","Female","Male"
    };
    private static final String[] SMOKING = {
        "No","No","Occasionally","No","Yes","No","Occasionally","No","No","Yes"
    };
    private static final String[] DRINKING = {
        "Occasionally","No","Yes","No","Occasionally","No","Yes","Occasionally","No","Yes"
    };
    private static final String[] RELIGION = {
        "Not Religious","Religious","Prefer not to say","Not Religious","Religious"
    };
    private static final String[] SLEEP = {
        "Early Sleeper","Night Owl","Flexible","Night Owl","Early Sleeper"
    };
    private static final String[] ROUTINE = {
        "Morning","Daytime","Evening","Late Night","Irregular"
    };
    private static final String[] CLEANLINESS = {
        "Very tidy","Mostly tidy","Relaxed","Mostly tidy","Very tidy"
    };
    private static final String[] SOCIAL = {
        "Mostly quiet","Balanced","Outgoing","Balanced","Mostly quiet"
    };
    private static final String[] GUESTS = {
        "Rarely","Sometimes","Often","Sometimes","Rarely"
    };

    public DemoProfileSeeder(UserRepository userRepository, RoommateProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        int inserted = 0;
        String encodedDemoPassword = passwordEncoder.encode("RoomieSync!2026");

        for (int i = 0; i < NAMES.length; i++) {
            String email = String.format("sample.roomie%02d@roomiesync.test", i + 1);
            if (userRepository.findByEmailIgnoreCase(email).isPresent()) continue;

            User user = new User();
            user.setName(NAMES[i]);
            user.setEmail(email);
            user.setPassword(encodedDemoPassword);
            user.setPhone(String.format("+91 88000%05d", i + 1));
            user.setGender(GENDERS[i % GENDERS.length]);
            user.setLocation(CITIES[i]);
            user.setAcademicYear(0);
            user = userRepository.save(user);

            RoommateProfile profile = new RoommateProfile();
            profile.setUser(user);
            profile.setPreferredGender(i % 5 == 0 ? "Any" : GENDERS[(i + 1) % GENDERS.length]);
            profile.setPreferredLocation(AREAS[i]);
            profile.setLifestyleSmoking(SMOKING[i % SMOKING.length]);
            profile.setLifestyleDrinking(DRINKING[i % DRINKING.length]);
            profile.setLifestyleReligion(RELIGION[i % RELIGION.length]);
            profile.setLifestyleSleep(SLEEP[i % SLEEP.length]);
            profile.setLifestyleRoutine(ROUTINE[i % ROUTINE.length]);
            profile.setLifestyleCleanliness(CLEANLINESS[i % CLEANLINESS.length]);
            profile.setLifestyleSocial(SOCIAL[i % SOCIAL.length]);
            profile.setLifestyleGuests(GUESTS[i % GUESTS.length]);

            // Mix strict and flexible requirements so match scores have meaningful variation.
            profile.setSmokerPreference(i % 4 == 0 ? "Doesn't matter" : SMOKING[(i + 1) % SMOKING.length]);
            profile.setDrinkerPreference(i % 4 == 1 ? "Doesn't matter" : DRINKING[(i + 2) % DRINKING.length]);
            profile.setReligiousPreference(i % 4 == 2 ? "Doesn't matter" : RELIGION[(i + 1) % RELIGION.length]);
            profile.setBedtimePreference(i % 4 == 3 ? "Flexible" : SLEEP[(i + 2) % SLEEP.length]);
            profile.setStudyTiming(i % 4 == 0 ? "Flexible" : ROUTINE[(i + 1) % ROUTINE.length]);
            profile.setCleanlinessPreference(i % 4 == 1 ? "Flexible" : CLEANLINESS[(i + 2) % CLEANLINESS.length]);
            profile.setSocialPreference(i % 4 == 2 ? "Flexible" : SOCIAL[(i + 1) % SOCIAL.length]);
            profile.setGuestPreference(i % 4 == 3 ? "Flexible" : GUESTS[(i + 2) % GUESTS.length]);

            profileRepository.save(profile);
            inserted++;
        }

        System.out.println("RoomieSync local sample data: added " + inserted + " of 50 fictional profiles.");
        System.out.println("Local test login password for all sample accounts: RoomieSync!2026");
    }
}
