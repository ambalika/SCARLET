package im.lostpilgr.scarlet.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

/**
 * DialogFragment to choose an account.
 *
 * @author Tom Black
 */
public class ChooseAccountDialogFragment extends DialogFragment {

    public static final String CHOOSE_ACCOUNT_DIALOG_TAG = "chooseAccount";

    private ChooseAccountCaller caller;
    private FragmentActivity fragmentActivity;
    private Account[] accounts;

    // Interface for caller of this dialog fragment.
    public interface ChooseAccountCaller {
        // Called when choose account is done.
        public void onChooseAccountDone();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            caller = (ChooseAccountCaller) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement " + ChooseAccountCaller.class.getSimpleName());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentActivity = getActivity();
        accounts = AccountManager.get(fragmentActivity).getAccountsByType("com.google");

        if (accounts.length == 1) {
            PreferencesUtils.setString(fragmentActivity, google_account_key, accounts[0].name);
            dismiss();
            caller.onChooseAccountDone();
            return;
        }

        String googleAccount = PreferencesUtils.getString(
                fragmentActivity, google_account_key, PreferencesUtils.GOOGLE_ACCOUNT_DEFAULT);
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i].name.equals(googleAccount)) {
                dismiss();
                caller.onChooseAccountDone();
                return;
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (accounts.length == 0) {
            return new AlertDialog.Builder(fragmentActivity).setMessage(
                    R.string.send_google_no_account_message).setTitle(R.string.send_google_no_account_title)
                    .setPositiveButton(R.string.generic_ok, null).create();
        }
        String[] choices = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            choices[i] = accounts[i].name;
        }
        return new AlertDialog.Builder(fragmentActivity).setNegativeButton(
                R.string.generic_cancel, null)
                .setPositiveButton(R.string.generic_ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        PreferencesUtils.setString(
                                fragmentActivity, R.string.google_account_key, accounts[position].name);
                        caller.onChooseAccountDone();
                    }
                }).setSingleChoiceItems(choices, 0, null)
                .setTitle(R.string.send_google_choose_account_title).create();
    }
}