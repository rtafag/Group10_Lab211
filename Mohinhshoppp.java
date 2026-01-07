import java.util.*;

// Thực thể Sản phẩm với biến thể
class Product {
    String id;
    String name;
    Map<String, Integer> variations; // Size/Màu -> Số lượng tồn kho

    public Product(String id, String name) {
        this.id = id;
        this.name = name;
        this.variations = new HashMap<>();
    }

    // Logic trừ kho chính xác (Thread-safe cho Flash Sale)
    public synchronized boolean updateInventory(String variant, int quantity) {
        int currentStock = variations.getOrDefault(variant, 0);
        if (currentStock >= quantity) {
            variations.put(variant, currentStock - quantity);
            return true;
        }
        return false;
    }
}

// Thực thể Voucher
class Voucher {
    String code;
    double discountValue;
    boolean isStackable; // Khuyến mãi chồng chéo
}

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlashSaleSimulator {
    public void startSimulation(Product product, String variant) {
        // Giả lập hàng nghìn User cùng đặt hàng một lúc bằng Thread Pool
        ExecutorService executor = Executors.newFixedThreadPool(100); 

        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> {
                boolean success = product.updateInventory(variant, 1);
                if (success) {
                    System.out.println("User đặt hàng thành công! Đơn hàng đang được tạo...");
                    // Logic tạo Order và tính toán Voucher ở đây
                } else {
                    System.err.println("Hết hàng!");
                }
            });
        }
        executor.shutdown();
    }
}
