package jeaps.foodtruck.common.truck.route;

import jeaps.foodtruck.common.truck.TruckDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RouteDAO {
    @Autowired
    private RouteRepository routeRepo;
    @Autowired
    private TruckDAO truckDAO;


    public void setRouteRepo(RouteRepository routeRepo) {
        this.routeRepo = routeRepo;
    }

    public void save(Route r){ this.routeRepo.save(r); }

    public void save(RouteDTO routeDTO){
        Route r = new Route();

        r.setDate(routeDTO.getDate());
        r.setLocation(routeDTO.getLocation());

        this.routeRepo.save(r);
    }

    public void deleteRoute(Integer routeID) {
        routeRepo.deleteById(routeID);
    }

    public List<Route> findByTruck(Integer truckID) {
        return this.routeRepo.findByTruck_id(truckID);
    }

}