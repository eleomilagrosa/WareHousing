package com.fusiotec.warehousing.warehousing.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Owner on 3/22/2017.
 */

public interface ApiService{
    //get original date
    @GET("date.php")
    Call<GenericReceiver> get_original_date();

    @FormUrlEncoded
    @POST("create_account.php")
    Call<GenericReceiver> create_account(@Field("first_name") String first_name,
                                         @Field("last_name") String last_name,
                                         @Field("username") String username,
                                         @Field("password") String password,
                                         @Field("email") String email,
                                         @Field("phone_no") String phone_no);

    @FormUrlEncoded
    @POST("update_account.php")
    Call<GenericReceiver> update_account(@Field("id") int id,
                                         @Field("first_name") String first_name,
                                         @Field("last_name") String last_name,
                                         @Field("email") String email,
                                         @Field("phone_no") String phone_no);
    @FormUrlEncoded
    @POST("approved_account.php")
    Call<GenericReceiver> approved_account(@Field("id") int id,
                                           @Field("approved_by") int approved_by);

    @FormUrlEncoded
    @POST("login.php")
    Call<GenericReceiver> login(@Field("username") String username,
                                @Field("password") String password);

    @FormUrlEncoded
    @POST("delete_account.php")
    Call<GenericReceiver> delete_account(@Field("id") int id);

    @FormUrlEncoded
    @POST("update_account_status_id.php")
    Call<GenericReceiver> update_account_status_id(@Field("id") int id,
                                                   @Field("account_type_id") int account_type_id);

    @FormUrlEncoded
    @POST("change_password.php")
    Call<GenericReceiver> change_password(@Field("id") int id,
                                          @Field("password") String password);

    @GET("get_stations.php")
    Call<GenericReceiver> get_stations(@Query("name") String name);

    @FormUrlEncoded
    @POST("create_station.php")
    Call<GenericReceiver> create_station(@Field("station_name") String station_name,
                                         @Field("station_prefix") String station_prefix,
                                         @Field("station_address") String station_address,
                                         @Field("station_number") String station_number,
                                         @Field("station_description") String station_description);
    @FormUrlEncoded
    @POST("update_station.php")
    Call<GenericReceiver> update_station(@Field("id") int id,
                                         @Field("station_name") String station_name,
                                         @Field("station_prefix") String station_prefix,
                                         @Field("station_address") String station_address,
                                         @Field("station_number") String station_number,
                                         @Field("station_description") String station_description);

    @FormUrlEncoded
    @POST("delete_stations.php")
    Call<GenericReceiver> delete_stations(@Field("id") int id);

    @FormUrlEncoded
    @POST("create_receive_session.php")
    Call<GenericReceiver> create_receive_session(@Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("update_receiving_session.php")
    Call<GenericReceiver> update_receiving_session(@Field("id") int id
                                                    , @Field("station_id") int station_id
                                                    , @Field("ref_no") String ref_no);

    @FormUrlEncoded
    @POST("create_receive_items.php")
    Call<GenericReceiver> create_receive_items(@Field("barcode") String barcode,
                                               @Field("desc") String desc,
                                               @Field("quantity") int quantity,
                                               @Field("item_id") int item_id,
                                               @Field("receiving_session_id") int receiving_session_id,
                                               @Field("user_id") int user_id,
                                               @Field("notes") String notes);

    @GET("get_accounts.php")
    Call<GenericReceiver> get_accounts(@Query("get_list_type") int get_list_type,
                                       @Query("name") String name,
                                       @Query("date") String date,
                                       @Query("direction") int direction);
    @GET("get_receiving_session.php")
    Call<GenericReceiver> get_receiving_session(@Query("get_list_type") int get_list_type);

    @GET("get_all_items.php")
    Call<GenericReceiver> get_all_items();

    @GET("get_all_locations.php")
    Call<GenericReceiver> get_all_locations();

    @GET("get_categories.php")
    Call<GenericReceiver> get_categories();

    @GET("get_receiving_session_by_id.php")
    Call<GenericReceiver> get_receiving_session_by_id(@Query("receiving_session_id") int receiving_session_id);

    @Multipart
    @POST("upload_image.php")
    Call<GenericReceiver> upload_image(@Part MultipartBody.Part uploaded_file,
                                                 @Part("label") RequestBody label,
                                                 @Part("meta_data") RequestBody meta_data,
                                                 @Part("meta_data_id")RequestBody meta_data_id);
}
