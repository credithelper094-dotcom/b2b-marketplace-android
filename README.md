# b2b-marketplace-android
 Полноценное переписывание устаревшего B2B-приложения на современный стек. Оптимизация производительности, оффлайн-режим, работа с очередями заказов
 
```text
app/
  src/main/java/com/example/b2bmarket/
    data/
      local/ (RoomDatabase.kt, OrderDao.kt)
      remote/ (ApiService.kt, OrderDto.kt)
      repository/ (OrderRepository.kt)
    domain/
      models/ (Order.kt)
      usecases/ (CreateOrderUseCase.kt)
    ui/
      order_list/ (OrderListScreen.kt, OrderListViewModel.kt)
      order_detail/ (OrderDetailScreen.kt)
    di/ (AppModule.kt)
    MainActivity.kt
build.gradle.kts
README.md

```
