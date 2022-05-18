package ServiceLayer.Objects;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public record AdministratorInfo(List<Administrator> administrators) {
    public AdministratorInfo(Collection<BusinessLayer.Products.Users.AdministratorInfo> administratorInfo) {
        this(administratorInfo.stream().map(Administrator::new).collect(Collectors.toList()));
    }


}
