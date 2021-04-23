package net.kirte;

import android.content.Intent;
import java.math.BigDecimal;
import dk.mobilepay.sdk.Country;
import dk.mobilepay.sdk.MobilePay;
import dk.mobilepay.sdk.ResultCallback;
import dk.mobilepay.sdk.model.FailureResult;
import dk.mobilepay.sdk.model.Payment;
import dk.mobilepay.sdk.model.SuccessResult;

public class MPConnection {
    private final LogWriter logger = LogWriter.getInstance();
    private static final MPConnection MPC = new MPConnection();
    private String refNum;
    private String price;

    public static MPConnection getInstance() { return MPC; }

    public MPConnection() {
        MobilePay.getInstance().init("APPFI0000000000", Country.FINLAND);
    }

    public void newPayment(String refNum, String price) {
        boolean isMobilePayInstalled = MobilePay.getInstance().isMobilePayInstalled(MainActivity.getInstance().getApplicationContext());
        if (isMobilePayInstalled) {
            this.refNum = refNum;
            this.price = price;
            Payment payment = new Payment();
            payment.setProductPrice(BigDecimal.valueOf(Double.parseDouble(price)));
            payment.setOrderId(refNum);
            Intent paymentIntent = MobilePay.getInstance().createPaymentIntent(payment);
            MainActivity.getInstance().startActivityForResult(paymentIntent, MainActivity.getInstance().getMobilepayPaymentRequestCode());
        } else {
            Intent intent = MobilePay.getInstance().createDownloadMobilePayIntent(MainActivity.getInstance().getApplicationContext());
            MainActivity.getInstance().startActivity(intent);
        }
    }

    public void handleResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.getInstance().getMobilepayPaymentRequestCode()) {
            MobilePay.getInstance().handleResult(resultCode, data, new ResultCallback() {
                @Override
                public void onSuccess(SuccessResult successResult) {
                    //TODO payment confirm to server
                    // Propably server side error when trying to update payment status
                    MainActivity.getInstance().showPaymentResultDialog(MainActivity.getInstance().getString(R.string.payment_successful_title),
                            MainActivity.getInstance().getString(R.string.payment_successful_message));
                    logger.log(refNum, price);
                    ReservationBook.getInstance().markAsPaid(refNum);
                }

                @Override
                public void onFailure(FailureResult failureResult) {
                    MainActivity.getInstance().showPaymentResultDialog(MainActivity.getInstance().getString(R.string.payment_failed_title),
                            MainActivity.getInstance().getString(R.string.payment_failed_message));
                }

                @Override
                public void onCancel(String s) {
                    MainActivity.getInstance().showPaymentResultDialog(MainActivity.getInstance().getString(R.string.payment_canceled_title),
                            MainActivity.getInstance().getString(R.string.payment_canceled_message));
                }
            });
        }
    }
}
