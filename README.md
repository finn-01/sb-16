


> Written with [StackEdit](https://stackedit.io/).
# @ConfigurationPropertie
### **Cấu hình đơn giản**

Giả sử, ứng dụng của tôi sẽ yêu cầu có một số giá trị toàn cục, mà thay vì cấu hình ở trong code, tôi muốn lưu nó ở bên ngoài, để tiện thay đổi mỗi khi cần.

Thì tôi sẽ làm như sau, tạo ra một class chứa các thông tin:

```java
@Data // Lombok, xem chi tiết tại bài viết
@Component // Là 1 spring bean
// @PropertySource("classpath:loda.yml") // Đánh dấu để lấy config từ trong file loda.yml
@ConfigurationProperties(prefix = "loda") // Chỉ lấy các config có tiền tố là "loda"
public class LodaAppProperties {
    private String email;
    private String googleAnalyticsId;

    // standard getters and setters
}
```

Chúng ta sử dụng `@Component` để Spring biết đây là một bean và khởi tạo nó.

Sử dụng `@PropertySource` để định nghĩa tên của file config. Nếu không có annotation này, Spring sẽ sử dụng file mặc định (_classpath:application.yml_ trong thư mục _resources_)

Cuối cùng là `@ConfigurationProperties`, annotation này đánh dấu class bên dưới nó là properties, các thuộc tính sẽ được tự động nạp vào khi Spring khởi tạo.

Lưu ý: các thuộc tính này được xác định bởi `prefix=loda`. Cái này bạn xem file _application.yml_ ở dưới sẽ hiểu.

Spring sẽ tự tìm các hàm setter để set giá trị cho các thuộc tính này, nên quan trọng là bạn phải tạo ra các setter method. (Ở đây tôi nhường việc đó cho [lombok](https://loda.me/general-huong-dan-su-dung-lombok-giup-code-java-nhanh-hon-69-loda1552789752787)).

Ngoài ra, để chạy được tính năng này, bạn cần kích hoạt nó bằng cách gắn `@EnableConfigurationProperties` lên một configuration nào đó. Ở đây tôi gắn lên hàm main luôn.

```java
@SpringBootApplication
@EnableConfigurationProperties
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

Vậy là xong, đơn giản phải không. Bây giờ, **Spring sẽ tự động bind toàn bộ giá trị từ trong file** _**application.yml**_ **vào bean** _**LodaAppProperties**_ **cho chúng ta.**

ạo ra file _application.yml_ tại thư mục resources:

![image](https://super-static-assets.s3.amazonaws.com/8a72ee8e-d4aa-4a06-985f-e92802c5bc44/images/fd8e05c4-a1fa-402d-bfa5-ea6c474b6aac.jpg?w=1500)

Thêm các thông tin chúng ta cần:

```makefile
loda:
  email: loda.namnh@gmail.com
  googleAnalyticsId: U-xxxxx
```

Chúng ta phải đặt các thuộc tính này sau prefix _loda_
### **Chạy thử**

Teedeee, thế là xong, rất đơn giản, để kiểm nghiệm xem Spring đã nhận các thông số cấu hình này chưa. Chúng ta sẽ in ra:

```java
@SpringBootApplication
@EnableConfigurationProperties
public class App implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Autowired LodaAppProperties lodaAppProperties;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Global variable:");
        System.out.println("\t Email: "+lodaAppProperties.getEmail());
        System.out.println("\t GA ID: "+lodaAppProperties.getGoogleAnalyticsId());
    }
}
```

Kết quả:

```makefile
Global variable:
	 Email: loda.namnh@gmail.com
	 GA ID: U-xxxxx
```

Bây giờ, ở bất kỳ đâu trong chương trình, khi cần lấy các thông tin config, tôi chỉ cần:

```java
@Autowired LodaAppProperties lodaAppProperties;
```

là xong.

### **Nested Properties**

Chúng ta có thể config các thuộc tính bên trong Class kể cả khi nó là `Lists`, `Maps` hay một class khác.

Bổ sung thêm thuộc tính:

```java
@Data // Lombok, xem chi tiết tại bài viết
@Component // Là 1 spring bean
//@PropertySource("classpath:loda.yml") // Đánh dấu để lấy config từ trong file loda.yml
@ConfigurationProperties(prefix = "loda") // Chỉ lấy các config có tiền tố là "loda"
public class LodaAppProperties {
    private String email;
    private String googleAnalyticsId;

    private List<String> authors;

    private Map<String, String> exampleMap;

    // standard getters and setters
}
```

Sửa file _application.yml_:

```makefile
loda:
  email: loda.namnh@gmail.com
  googleAnalyticsId: U-xxxxx
  authors:
    - loda
    - atom
  exampleMap:
    key1: hello
    key2: world
```

Chạy lại chương trình:

```java
@Override
public void run(String... args) throws Exception {
    System.out.println("Global variable:");
    System.out.println("\t Email: " + lodaAppProperties.getEmail());
    System.out.println("\t GA ID: " + lodaAppProperties.getGoogleAnalyticsId());
    System.out.println("\t Authors: " + lodaAppProperties.getAuthors());
    System.out.println("\t Example Map: " + lodaAppProperties.getExampleMap());
}
```

Kết quả:

```makefile
Global variable:
	 Email: loda.namnh@gmail.com
	 GA ID: U-xxxxx
	 Authors: [loda, atom]
	 Example Map: {key1=hello, key2=world}
```
