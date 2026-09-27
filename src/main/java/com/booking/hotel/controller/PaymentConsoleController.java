package com.booking.hotel.controller;

import com.booking.hotel.model.Booking;
import com.booking.hotel.model.Payment;
import com.booking.hotel.service.PaymentService;
import com.booking.hotel.service.PaymentServiceImpl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentConsoleController {

    private static final Logger logger = Logger.getLogger(PaymentConsoleController.class.getName());
    private final PaymentService paymentService;
    private final Scanner scanner;

    public PaymentConsoleController() {
        this.paymentService = new PaymentServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    public void startPaymentMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- PAYMENT MANAGEMENT MENU ---");
            System.out.println("1. Process New Payment");
            System.out.println("2. View Payment by ID");
            System.out.println("3. View Payment by Booking ID");
            System.out.println("4. Refund Payment");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        processPaymentPrompt();
                        break;
                    case "2":
                        viewPaymentByIdPrompt();
                        break;
                    case "3":
                        viewPaymentByBookingPrompt();
                        break;
                    case "4":
                        refundPaymentPrompt();
                        break;
                    case "5":
                        running = false;
                        System.out.println("Exiting Payment Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                logger.log(Level.SEVERE, "Payment console controller error", e);
            }
        }
    }

    private void processPaymentPrompt() throws Exception {
        System.out.print("Enter Booking ID: ");
        long bookingId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter Amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        System.out.print("Enter Payment Method (e.g., CREDIT_CARD, UPI): ");
        String method = scanner.nextLine();

        System.out.print("Enter Transaction Reference Code: ");
        String txRef = scanner.nextLine();

        Booking booking = new Booking();
        booking.setBookingId(bookingId);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setPaymentStatus("PAID");
        payment.setTransactionRef(txRef);
        payment.setPaidAt(Timestamp.valueOf(LocalDateTime.now().withNano(0)));

        boolean success = paymentService.processPayment(payment);
        if (success) {
            System.out.println("Success! Payment recorded with ID: " + payment.getPaymentId());
        } else {
            System.out.println("Failed to process payment.");
        }
    }

    private void viewPaymentByIdPrompt() throws Exception {
        System.out.print("Enter Payment ID: ");
        long paymentId = Long.parseLong(scanner.nextLine());

        Payment payment = paymentService.getPaymentById(paymentId);
        if (payment != null) {
            System.out.println("\n[Payment Found]");
            System.out.println("Payment ID: " + payment.getPaymentId());
            System.out.println("Booking ID: " + payment.getBooking().getBookingId());
            System.out.println("Amount: " + payment.getAmount());
            System.out.println("Method: " + payment.getPaymentMethod());
            System.out.println("Status: " + payment.getPaymentStatus());
            System.out.println("Transaction Ref: " + payment.getTransactionRef());
            System.out.println("Paid At: " + payment.getPaidAt());
        } else {
            System.out.println("No payment found with ID: " + paymentId);
        }
    }

    private void viewPaymentByBookingPrompt() throws Exception {
        System.out.print("Enter Booking ID: ");
        long bookingId = Long.parseLong(scanner.nextLine());

        Payment payment = paymentService.getPaymentByBookingId(bookingId);
        if (payment != null) {
            System.out.println("\n[Payment Found for Booking]");
            System.out.println("Payment ID: " + payment.getPaymentId());
            System.out.println("Amount: " + payment.getAmount());
            System.out.println("Status: " + payment.getPaymentStatus());
            System.out.println("Transaction Ref: " + payment.getTransactionRef());
        } else {
            System.out.println("No payment found for booking ID: " + bookingId);
        }
    }

    private void refundPaymentPrompt() throws Exception {
        System.out.print("Enter Payment ID to refund: ");
        long paymentId = Long.parseLong(scanner.nextLine());

        boolean success = paymentService.refundPayment(paymentId);
        if (success) {
            System.out.println("Payment ID " + paymentId + " has been successfully marked as REFUNDED.");
        } else {
            System.out.println("Failed to refund payment.");
        }
    }
}

