package com.kirch.poseidon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kirch.poseidon.dummy.DummyContent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static View rootView;
    public static int viewSwitch = 0;


    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        switch (mItem.id) {
            case "pos":
                viewSwitch = 0;
                rootView = inflater.inflate(R.layout.activity_pos, container, false);
                Button itemScannedButton = (Button) rootView.findViewById(R.id.scanButton);
                EditText etItemSerial = (EditText) rootView.findViewById(R.id.etSerialPOS);
                itemScannedButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), ScannerActivity.class);
                        startActivity(intent);

                    }

                });
                break;
            case "return":
                rootView = inflater.inflate(R.layout.return_item, container, false);
                break;
            case "createAccount":
                rootView = inflater.inflate(R.layout.activity_create_account, container, false);
                Button creatAccountButton = (Button) rootView.findViewById(R.id.bCreateAccount);
                final EditText etName = (EditText) rootView.findViewById(R.id.etName);
                final EditText etPhoneNumber = (EditText) rootView.findViewById(R.id.etPhone);
                final EditText etEmail = (EditText) rootView.findViewById(R.id.etEmail);
                final EditText etNotes = (EditText) rootView.findViewById(R.id.etNotes);
                creatAccountButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ParseObject customer = new ParseObject("Customer");
                        customer.put("Name", etName.getText().toString());
                        customer.put("Phone", Integer.parseInt( etPhoneNumber.getText().toString()));
                        customer.put("email", etEmail.getText().toString());
                        customer.put("notes", etNotes.getText().toString());
                        customer.saveInBackground();
                    }

                });
                break;
            case "transactionLookup":
                rootView = inflater.inflate(R.layout.transaction_lookup, container, false);
                break;
            case "orderInvetory":
                rootView = inflater.inflate(R.layout.order_inventory, container, false);
                break;
            case "addInventory":
                viewSwitch = 1;
                rootView = inflater.inflate(R.layout.add_inventory, container, false);
                Button scanInventoryItemButton = (Button) rootView.findViewById(R.id.bScanInventory);
                Button addInventoryButton = (Button) rootView.findViewById(R.id.bAddInventory);
                final EditText etSerialNumber = (EditText) rootView.findViewById(R.id.etItemSerial);
                final EditText etProductName = (EditText) rootView.findViewById(R.id.etItemName);
                final EditText etItemQuantity = (EditText) rootView.findViewById(R.id.etItemQuantity);
                final EditText etItemPrice = (EditText) rootView.findViewById(R.id.etItemPrice);
                scanInventoryItemButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), ScannerActivity.class);
                        startActivity(intent);
                    }

                });
                addInventoryButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ParseObject inventory = new ParseObject("Inventory");
                        inventory.put("UPC", Integer.parseInt(etSerialNumber.getText().toString()));
                        inventory.put("Product_Name", etProductName.getText().toString());
                        inventory.put("Stock", Integer.parseInt( etItemQuantity.getText().toString()));
                        inventory.put("Price", Double.parseDouble(etItemPrice.getText().toString()));
                        inventory.saveInBackground();
                    }

                });
                break;
            case "removeInvetory":
                rootView = inflater.inflate(R.layout.remove_inventory, container, false);
                break;
            case "manageAccounts":
                rootView = inflater.inflate(R.layout.manage_accounts, container, false);
                break;
            default:
                rootView = inflater.inflate(R.layout.activity_pos, container, false);
        }


        return rootView;
    }

    public static void recievedScan(String serialNumber){

        switch (viewSwitch){
            case 0:

                EditText etItemSerial = (EditText) rootView.findViewById(R.id.etSerialPOS);
                etItemSerial.setText(serialNumber);

                int serial = Integer.parseInt(etItemSerial.getText().toString());

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Inventory");
                query.whereEqualTo("UPC", serial);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> items, ParseException e) {
                        if (e == null) {
                            Log.d("score", "Retrieved " + items.size() + " item");
                        } else {
                            Log.d("score", "Error: " + e.getMessage());
                        }
                    }
                });

                break;

            case 1:

                final EditText etSerialNumber = (EditText) rootView.findViewById(R.id.etItemSerial);
                etSerialNumber.setText(serialNumber);

                break;

            default:
                break;
        }


    }
}
