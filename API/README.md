# CareU API - ASP.NET Core Web API

## 📋 Mô tả dự án

CareU API là một RESTful Web API được phát triển bằng ASP.NET Core 8, cung cấp backend services cho nền tảng đặt dịch vụ dọn dẹp nhà cửa. API hỗ trợ đầy đủ các chức năng từ authentication, booking management, đến news system và admin operations.

### 🎯 Chức năng chính
- **Authentication & Authorization**: Đăng ký, đăng nhập, quản lý profile
- **Booking Management**: Đặt lịch dọn dẹp, quản lý booking
- **Service Management**: Quản lý dịch vụ và giá cả
- **User Management**: Quản lý người dùng (User, Cleaner, Admin)
- **News System**: Quản lý tin tức và bài viết
- **Review System**: Đánh giá và review
- **Admin Dashboard**: Thống kê và quản lý hệ thống

## 🛠️ Công nghệ sử dụng

### Core Framework
- **ASP.NET Core 8.0**
- **Entity Framework Core 8.0.16**
- **SQL Server Database**
- **C# Programming Language**

### Authentication & Security
- **JWT Bearer Authentication** (configured but disabled in current version)
- **Password Hashing & Validation**
- **Role-based Access Control**

### API Documentation
- **Swagger/OpenAPI 6.4.0**
- **Auto-generated API documentation**
- **Interactive API testing interface**

### Additional Features
- **CORS Support**: Cross-origin resource sharing
- **Data Validation**: Model validation với Data Annotations
- **Error Handling**: Comprehensive exception handling
- **Email Configuration**: SMTP settings for email services

## 📁 Cấu trúc dự án

```
Exe201_API/
├── Controllers/           # API Controllers
│   ├── AuthController.cs      # Authentication endpoints
│   ├── BookingsController.cs  # Booking management
│   ├── AdminController.cs     # Admin operations
│   ├── CleanerController.cs   # Cleaner-specific operations
│   ├── NewsController.cs      # News management
│   ├── ReviewController.cs    # Review system
│   ├── UserController.cs      # User management
│   └── ReferenceDataController.cs # Reference data
├── Models/               # Entity Models
│   ├── CareUContext.cs       # DbContext
│   ├── User.cs              # User entity
│   ├── Booking.cs           # Booking entity
│   ├── Service.cs           # Service entity
│   ├── NewsArticle.cs       # News entity
│   └── ...                  # Other entities
├── Services/             # Business Logic
│   ├── IAuthService.cs       # Auth service interface
│   ├── AuthService.cs        # Authentication logic
│   ├── IBookingService.cs    # Booking service interface
│   ├── BookingService.cs     # Booking business logic
│   ├── INewsService.cs       # News service interface
│   ├── NewsService.cs        # News management logic
│   └── ...                   # Other services
├── DTOs/                 # Data Transfer Objects
│   ├── LoginRequestDto.cs    # Login request model
│   ├── RegisterRequestDto.cs # Registration model
│   ├── CreateBookingRequestDto.cs # Booking creation
│   ├── News/                 # News-related DTOs
│   └── ...                   # Other DTOs
├── Program.cs           # Application startup
├── appsettings.json    # Configuration
└── Exe201_API.csproj   # Project file
```

## 🗄️ Database Schema

### Core Entities
- **Users**: Thông tin người dùng (User, Cleaner, Admin)
- **Services**: Dịch vụ dọn dẹp với giá cơ bản
- **AreaSizes**: Kích thước diện tích và hệ số nhân giá
- **TimeSlots**: Khung giờ làm việc
- **Bookings**: Đặt lịch dọn dẹp
- **Reviews**: Đánh giá và review
- **Payments**: Thanh toán
- **Notifications**: Thông báo
- **NewsArticles**: Tin tức và bài viết
- **NewsCategories**: Danh mục tin tức
- **NewsTags**: Tags cho bài viết

### Relationships
- **User-Booking**: One-to-Many (User có nhiều bookings)
- **Service-Booking**: One-to-Many (Service có nhiều bookings)
- **Cleaner-Booking**: One-to-Many (Cleaner được phân công nhiều bookings)
- **NewsArticle-NewsCategory**: Many-to-One (Article thuộc về Category)
- **NewsArticle-NewsTag**: Many-to-Many (Article có nhiều tags)

## 🔌 API Endpoints

### Authentication (`/api/auth`)
- `POST /login` - Đăng nhập
- `POST /register` - Đăng ký tài khoản
- `POST /change-password` - Đổi mật khẩu

### Bookings (`/api/bookings`)
- `POST /` - Tạo booking mới
- `GET /` - Lấy danh sách booking của user
- `GET /{id}` - Lấy chi tiết booking
- `GET /dashboard-stats` - Thống kê dashboard

### Admin (`/api/admin`)
- `GET /dashboard-stats` - Thống kê admin dashboard
- `GET /users` - Quản lý người dùng
- `GET /bookings` - Quản lý tất cả bookings
- `POST /assign-cleaner` - Phân công cleaner

### Cleaner (`/api/cleaner`)
- `GET /dashboard-stats` - Thống kê cleaner dashboard
- `GET /assigned-bookings` - Danh sách booking được phân công
- `PUT /update-job-status` - Cập nhật trạng thái công việc

### News (`/api/news`)
- `GET /articles` - Lấy danh sách bài viết
- `GET /articles/{id}` - Lấy chi tiết bài viết
- `GET /categories` - Lấy danh mục tin tức
- `POST /articles` - Tạo bài viết mới (Admin)

### Reviews (`/api/reviews`)
- `POST /` - Tạo review mới
- `GET /booking/{bookingId}` - Lấy review theo booking
- `GET /cleaner/{cleanerId}` - Lấy review theo cleaner

## 🔧 Cài đặt và chạy

### Yêu cầu hệ thống
- **.NET 8 SDK**
- **SQL Server 2019+**
- **Visual Studio 2022** hoặc **VS Code**

### Bước 1: Clone repository
```bash
git clone <repository-url>
cd PRM392/API/Exe201_API
```

### Bước 2: Cấu hình Database
1. Tạo database `CareU` trong SQL Server
2. Cập nhật connection string trong `appsettings.json`:
   ```json
   "ConnectionStrings": {
       "MyCnn": "server=localhost; database=CareU;uid=sa;pwd=123; TrustServerCertificate=True;"
   }
   ```

### Bước 3: Restore packages và build
```bash
dotnet restore
dotnet build
```

### Bước 4: Chạy migrations (nếu cần)
```bash
dotnet ef database update
```

### Bước 5: Chạy API
```bash
dotnet run
```

### Bước 6: Truy cập Swagger
- Mở browser: `https://localhost:7000/swagger`
- Hoặc: `http://localhost:5000/swagger`

## 🔒 Security Features

### Authentication (Currently Disabled)
- **JWT Bearer Token**: Secure API authentication
- **Password Validation**: Strong password requirements
- **Role-based Authorization**: User, Cleaner, Admin roles

### Data Protection
- **Input Validation**: Model validation với Data Annotations
- **SQL Injection Prevention**: Entity Framework protection
- **CORS Configuration**: Cross-origin security
- **Error Handling**: Comprehensive exception handling

## 📊 Business Logic

### Booking System
- **Service Selection**: Chọn dịch vụ dọn dẹp
- **Area Size Calculation**: Tính giá theo diện tích
- **Time Slot Booking**: Đặt khung giờ làm việc
- **Cleaner Assignment**: Phân công cleaner
- **Status Tracking**: Theo dõi trạng thái booking

### Pricing System
- **Base Price**: Giá cơ bản cho mỗi dịch vụ
- **Area Multiplier**: Hệ số nhân theo diện tích
- **Dynamic Pricing**: Tính giá động
- **Transparent Pricing**: Giá cả minh bạch

### User Management
- **Role-based System**: 3 vai trò chính
- **Profile Management**: Cập nhật thông tin cá nhân
- **Status Management**: Active, Pending, Inactive
- **Experience Tracking**: Kinh nghiệm cho cleaner

## 📧 Email Configuration

### SMTP Settings
```json
"SmtpSettings": {
    "Server": "smtp.gmail.com",
    "Port": 587,
    "User": "watchshop1804@gmail.com",
    "Password": "pbbv nfkv fkjo ixfz",
    "Timeout": 10000
}
```

### Email Features
- **Account Verification**: Email xác thực tài khoản
- **Booking Confirmations**: Xác nhận đặt lịch
- **Status Updates**: Cập nhật trạng thái booking
- **System Notifications**: Thông báo hệ thống

## 🧪 Testing

### API Testing
- **Swagger UI**: Interactive API testing
- **Postman**: Manual API testing
- **Unit Testing**: Service layer testing
- **Integration Testing**: End-to-end testing

### Database Testing
- **Entity Framework**: Database operations testing
- **Migration Testing**: Schema changes testing
- **Data Validation**: Business rule testing

## 📈 Performance & Optimization

### Database Optimization
- **Indexing**: Optimized database indexes
- **Query Optimization**: Efficient LINQ queries
- **Connection Pooling**: Database connection management

### API Performance
- **Async/Await**: Non-blocking operations
- **Caching**: Response caching strategies
- **Compression**: Response compression
- **Rate Limiting**: API rate limiting

## 🐛 Known Issues & Future Improvements

### Current Limitations
- **Authentication Disabled**: JWT authentication is configured but not active
- **Limited Error Logging**: Basic exception handling
- **No Caching**: No response caching implementation

### Planned Improvements
- **Enable JWT Authentication**: Activate secure authentication
- **Implement Caching**: Add response caching
- **Add Logging**: Comprehensive logging system
- **Rate Limiting**: API rate limiting
- **API Versioning**: Version control for APIs

## 📝 API Documentation

### Swagger Documentation
- **Auto-generated**: From code comments and attributes
- **Interactive Testing**: Test APIs directly from browser
- **Schema Documentation**: Request/response schemas
- **Authentication**: JWT token integration

### Code Documentation
- **XML Comments**: Detailed method documentation
- **DTO Validation**: Input validation documentation
- **Error Codes**: Standardized error responses
- **Examples**: Usage examples

## 📞 Liên hệ

- **Email**: developer@careu.com
- **Project Link**: [https://github.com/your-username/Exe201_API](https://github.com/your-username/Exe201_API)

---

🔧 **API Documentation**: Truy cập `/swagger` để xem đầy đủ API documentation và test các endpoints. 