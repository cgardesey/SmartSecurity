package security.smart.smartsecurity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class LoadCreditDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        EditText inputET = (EditText) inflater.inflate(R.layout.dialog_load_credit, null);
        builder.setTitle("Load Credit");
        builder.setView(inputET);

        inputET.setHint("*134*voucher#");
        // Set up the buttons
        builder.setPositiveButton("Load", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RemoteMessageOps remoteMessageOps = new RemoteMessageOps(requireContext().getApplicationContext());
                String voucherStr = inputET.getText().toString().trim();
                String rechargeStr = "*134*" + voucherStr + "#";
                remoteMessageOps.loadCredit(rechargeStr);
                dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }
}
