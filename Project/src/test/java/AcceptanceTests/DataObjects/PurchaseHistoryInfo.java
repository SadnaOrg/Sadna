package AcceptanceTests.DataObjects;

import java.util.List;
import java.util.stream.Collectors;

public class PurchaseHistoryInfo {
    public List<PurchaseHistory> historyInfo;

    public PurchaseHistoryInfo(ServiceLayer.Objects.PurchaseHistoryInfo purchaseHistory){
        this.historyInfo = purchaseHistory.historyInfo().stream().map(PurchaseHistory::new).collect(Collectors.toList());
    }
}
