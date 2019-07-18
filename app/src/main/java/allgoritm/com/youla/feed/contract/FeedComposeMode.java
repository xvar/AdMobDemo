package allgoritm.com.youla.feed.contract;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@IntDef({
        FeedComposeMode.ALL,
        FeedComposeMode.PRODUCTS_ADVERT
})
public @interface FeedComposeMode {
    int ALL = 10101;
    int PRODUCTS_ADVERT = 10102;
}
