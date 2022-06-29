package ServiceLayer.Objects;

import java.util.concurrent.ConcurrentHashMap;

public record BidOffer (int shopID ,ConcurrentHashMap<Integer, ApproveBid> approvals){
    public BidOffer(BusinessLayer.Users.BidOffer bidOffer)
    {
        this(bidOffer.getShopID(), new ConcurrentHashMap<>());
        for(Integer pid: bidOffer.getApprovals().keySet())
        {
            approvals.put(pid,new ApproveBid(bidOffer.getApprovals().get(pid),bidOffer.getUserName()));
        }
    }
}
