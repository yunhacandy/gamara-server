package gamara.server.converter;

import gamara.server.domain.entity.StoreRecommend;

public class StoreConverter {

    public static StoreRecommend toEntity(long userId, long storeId){
        return StoreRecommend.builder()
                .userId(userId)
                .storeId(storeId)
                .build();
    }
}
