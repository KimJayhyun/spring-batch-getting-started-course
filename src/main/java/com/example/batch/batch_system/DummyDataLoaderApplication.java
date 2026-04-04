package com.example.batch.batch_system;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DummyDataLoaderApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(DummyDataLoaderApplication.class, args);
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);

        // 기존 데이터 초기화
        jdbcTemplate.execute("DELETE FROM orders");
        jdbcTemplate.execute("DELETE FROM settlement");

        String[] stores = {"엽기떡볶이", "교촌치킨", "피자헛", "스타벅스", "김밥천국"};
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(7);

        Random random = new Random();
        List<Object[]> batch = new ArrayList<>();
        String sql =
                "INSERT INTO orders (customer_name, store_name, amount, order_date) VALUES (?, ?, ?, ?)";

        for (int i = 1; i <= 1_000_000; i++) {
            String customerName = "고객_" + i;
            String storeName = stores[random.nextInt(stores.length)];
            int amount = 1000 + random.nextInt(50000);

            LocalDate orderDate;
            if (random.nextDouble() < 0.7) {
                orderDate = sevenDaysAgo;
            } else {
                orderDate = today.minusDays(1 + random.nextInt(10));
            }

            batch.add(new Object[] {customerName, storeName, amount,
                    java.sql.Date.valueOf(orderDate)});

            if (i % 10000 == 0) {
                jdbcTemplate.batchUpdate(sql, batch);
                batch.clear();
                System.out.println(i + "건 완료...");
            }
        }

        if (!batch.isEmpty()) {
            jdbcTemplate.batchUpdate(sql, batch);
        }

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM orders", Integer.class);
        System.out.println("총 주문 수: " + count);

        context.close();
    }
}
