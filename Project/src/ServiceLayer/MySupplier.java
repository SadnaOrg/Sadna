package ServiceLayer;

import javax.naming.NoPermissionException;

public interface MySupplier<T> {
     T get() throws NoPermissionException;
}
