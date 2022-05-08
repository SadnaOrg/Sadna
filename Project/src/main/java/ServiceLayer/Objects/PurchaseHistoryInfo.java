package ServiceLayer.Objects;



import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public record PurchaseHistoryInfo(List<PurchaseHistory> historyInfo) {
    public PurchaseHistoryInfo(Collection<BusinessLayer.Shops.PurchaseHistory> historyInfo) {
        this(historyInfo.stream().map(h-> new PurchaseHistory(h)).collect(Collectors.toList()));
    }
}
