package jeaps.foodtruck.common.user.customer;


import jeaps.foodtruck.common.truck.Truck;
import jeaps.foodtruck.common.truck.TruckDAO;
import jeaps.foodtruck.common.user.customer.preferences.Preferences;
import jeaps.foodtruck.common.user.customer.preferences.PreferencesDAO;
import jeaps.foodtruck.common.user.user.User;
import jeaps.foodtruck.common.user.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * A class to interact with the Customer table in the database
 */
@Repository
public class CustomerDAO {

    //Constants to use with getting recommendations
    private static Integer NUM_RECS = 10;
    private static Integer MAX_DISTANCE = 30;
    private static Integer DISTANCE_FILTER = 10;

    //The repository used to store Customer objects
    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private TruckDAO truckDAO;

    @Autowired
    private PreferencesDAO preferencesDAO;

    /**
     * Saves the Customer object in the database
     * @param c The Customer object to be saved
     */
    public void save(Customer c) {
        this.customerRepo.save(c);
    }

    /**
     * Saves a Customer object with the given ID in the database
     * @param id The ID of the Customer to be saved
     */
    public void save(Integer id){
        //Creates the Customer and sets the ID
        Customer c = new Customer();
        c.setId(id);
        //c.preference = new Preferences(id);
        //Saves the customer in the database
        this.save(c);

    }

    public List<Object> getSubscribedTrucks(String username) {
        List<Object> returns = new ArrayList<>();
        User user = userDAO.findByUsername(username);
        Optional<Customer> customer = customerRepo.findById(user.getId());


        if(customer.isPresent()) {

            List<Object> userInfo = new ArrayList<>();
            userInfo.add(user.getId());
            userInfo.add(user.getUsername());
            returns.add(userInfo);

            returns.addAll(customer.get().getTrucks());

            return returns;
        }
        return null;

    }

    public void subscribeToTruck(String username, Integer truckID) {
        User user = userDAO.findByUsername(username);
        Optional<Customer> customer = customerRepo.findById(user.getId());
        Optional<Truck> truck = truckDAO.findById(truckID);


        if(customer.isPresent() && truck.isPresent()) {



            List<Customer> customers = truck.get().getCustomers();
            customers.add(customer.get());
            truck.get().setCustomers(customers);

            List<Truck> trucks = customer.get().getTrucks();
            trucks.add(truck.get());
            customer.get().setTrucks(trucks);


            customerRepo.save(customer.get());


        } //HOW ARE WE THROWING ERRORS AGAIN?????
    }

    public void unsubscribeToTruck(String username, Integer truckID) {
        User user = userDAO.findByUsername(username);
        Optional<Customer> customer = customerRepo.findById(user.getId());
        Optional<Truck> truck = truckDAO.findById(truckID);


        if(customer.isPresent() && truck.isPresent()) {


            List<Customer> customers = truck.get().getCustomers();
            customers.remove(customer.get());
            truck.get().setCustomers(customers);

            List<Truck> trucks = customer.get().getTrucks();
            trucks.remove(truck.get());
            customer.get().setTrucks(trucks);


            customerRepo.save(customer.get());
        } //HOW ARE WE THROWING ERRORS AGAIN?????
    }
    public List<Truck> getRecommendations(String username) {
        //Initialise the list of trucks to return
        List<Truck> suggestions = new ArrayList<Truck>();

        //Get the user who we are providing recommendations for
        User user = userDAO.findByUsername(username);
        //Get the preferences of the user
        Optional<Preferences> userPrefs = preferencesDAO.findById(user.getId());

        //Get all trucks within a set distance                  *********right now there is no distance calculation***********
        suggestions = truckDAO.findALL();

        //If there are no preferences, return a random set of trucks
        if(!userPrefs.isPresent()){
            return suggestions.subList(0, NUM_RECS-1);
        }

        //Create a map to sort trucks based on scores
        Map<Integer, List<Truck>> truckScores = new HashMap<Integer, List<Truck>>();

        int highscore = 0;
        for(Truck t : suggestions){
            int score = getScore(t, userPrefs.get());
            if(score > highscore){highscore = score;}

            //If the truck does not contain the score, add it
            if(!truckScores.containsKey(score)){
                truckScores.put(score, new ArrayList<Truck>());
            }
            //Add the truck to the right score bracket
            truckScores.get(score).add(t);
        }

        suggestions = null;

        while(highscore >= 0 && suggestions.size() < NUM_RECS){
            for(Truck t : truckScores.get(highscore)){
                if(suggestions.size() < NUM_RECS){
                    suggestions.add(t);
                }
            }
            highscore--;
        }

        return suggestions;
    }

    public Integer getScore(Truck truck, Preferences prefs){
        int score = 0;

        if(truck.getType() == prefs.getFoodPref()){
            score += 1;
        }
        if(truck.getPrice().getFloor() <= prefs.getMaxPricePref().getFloor()){
            score += 2;
        }

        return score;
    }
}

