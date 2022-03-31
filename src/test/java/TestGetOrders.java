import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import test.api.settings.client.OrderClient;
import test.api.settings.model.FullOrder;

import static org.junit.Assert.assertFalse;

public class TestGetOrders {
    @Test
    @DisplayName("Запрос возвращает список заказов в теле ответа")
    public void getOrdersLimit10ReturnListOfOrders() {
        FullOrder response = OrderClient.getOrders(10).body().as(FullOrder.class);
        assertFalse(response.getOrders().isEmpty());
    }
}


