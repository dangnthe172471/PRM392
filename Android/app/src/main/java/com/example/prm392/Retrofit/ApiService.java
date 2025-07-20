package com.example.prm392.Retrofit;

import com.example.prm392.model.LoginRequest;
import com.example.prm392.model.RegisterRequest;
import com.example.prm392.model.LoginResponse;
import com.example.prm392.model.UserProfile;
import com.example.prm392.model.UserProfileUpdateRequest;
import com.example.prm392.model.ForgotPasswordRequest;
import com.example.prm392.model.VerifyPinRequest;
import com.example.prm392.model.ResetPasswordRequest;
import com.example.prm392.model.ApiResponse;
import com.example.prm392.model.Job;
import com.example.prm392.model.UpdateJobStatusRequest;
import com.example.prm392.model.CleanerDashboardStats;
import com.example.prm392.model.BookingResponse;
import com.example.prm392.model.ReviewResponse;
import com.example.prm392.model.CreateReviewRequest;
import com.example.prm392.model.CreateBookingRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import com.example.prm392.model.NewsArticle;
import com.example.prm392.model.NewsListResponse;
import com.example.prm392.model.CreateNewsArticleRequest;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            .create();

    ApiService api = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5266/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/Auth/register")
    Call<LoginResponse> register(@Body RegisterRequest request);

    @GET("api/User/profile")
    Call<UserProfile> getUserProfile(@Query("userId") int userId);

    @PUT("api/User/profile")
    Call<UserProfile> updateUserProfile(
        @Body UserProfileUpdateRequest request,
        @Query("userId") int userId
    );

    @POST("api/auth/forgot-password")
    Call<ApiResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("api/auth/verify-pin")
    Call<ApiResponse> verifyPin(@Body VerifyPinRequest request);

    @POST("api/auth/reset-password")
    Call<ApiResponse> resetPassword(@Body ResetPasswordRequest request);

    // Lấy danh sách việc có sẵn cho cleaner
    @GET("api/Cleaner/available-jobs")
    Call<List<Job>> getAvailableJobs();

    // Lấy danh sách việc đã nhận của cleaner
    @GET("api/Cleaner/my-jobs")
    Call<List<Job>> getMyJobs(@Query("cleanerId") int cleanerId, @Query("status") String status);

    // Nhận một việc mới
    @POST("api/Cleaner/accept-job/{bookingId}")
    Call<Void> acceptJob(@Path("bookingId") int bookingId, @Query("cleanerId") int cleanerId);

    // Cập nhật trạng thái công việc
    @PUT("api/Cleaner/update-job-status/{bookingId}")
    Call<Void> updateJobStatus(@Path("bookingId") int bookingId, @Query("cleanerId") int cleanerId, @Body UpdateJobStatusRequest request);

    // Lấy thông tin cá nhân cleaner
    @GET("api/cleaner/profile")
    Call<UserProfile> getCleanerProfile(@Query("cleanerId") int cleanerId);

    // Lấy dashboard stats cho cleaner
    @GET("api/cleaner/dashboard-stats")
    Call<CleanerDashboardStats> getCleanerDashboardStats(@Query("cleanerId") int cleanerId);

    // Booking APIs
    @GET("api/Bookings")
    Call<List<BookingResponse>> getUserBookings(@Query("status") String status, @Query("userId") int userId);

    @GET("api/Bookings/{id}")
    Call<BookingResponse> getBookingById(@Path("id") int bookingId, @Query("userId") int userId);

    @POST("api/Bookings")
    Call<BookingResponse> createBooking(@Body CreateBookingRequest request, @Query("userId") int userId);

    @GET("api/Review/booking/{bookingId}")
    Call<ReviewResponse> getReviewByBooking(@Path("bookingId") int bookingId);

    @POST("api/Review")
    Call<ReviewResponse> createReview(@Body CreateReviewRequest request, @Query("userId") int userId);

    @PUT("api/Review/{bookingId}")
    Call<ReviewResponse> updateReview(@Path("bookingId") int bookingId, @Body CreateReviewRequest request, @Query("userId") int userId);

    // Reference Data APIs
    @GET("api/ReferenceData/services")
    Call<List<com.example.prm392.model.ServiceModel>> getServices();

    @GET("api/ReferenceData/areasizes")
    Call<List<com.example.prm392.model.AreaSizeModel>> getAreaSizes();

    @GET("api/ReferenceData/timeslots")
    Call<List<com.example.prm392.model.TimeSlotModel>> getTimeSlots();

    @GET("api/news")
    Call<NewsListResponse> getNewsArticles(@Query("page") int page, @Query("pageSize") int pageSize);

    @GET("api/news/categories")
    Call<List<NewsArticle.Category>> getCategories();

    @POST("api/news")
    Call<NewsArticle> createArticle(@Body CreateNewsArticleRequest request);
} 