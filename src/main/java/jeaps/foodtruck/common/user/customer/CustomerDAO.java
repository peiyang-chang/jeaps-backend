package jeaps.foodtruck.common.user.customer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class CustomerDAO {


    @Autowired
    private CustomerRepository userRepo;


    public void save(Customer c){
        this.userRepo.save(c);
    }

    public void save(Integer id){
        Customer c = new Customer();
        c.setId(id);

        this.userRepo.save(c);

    }

}
