package com.kirch.poseidon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kirch.poseidon.dummy.DummyContent;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    static ListView listView;

    //double declarations
    static double subtotal = 0;
    static double taxRate = 0.0615;
    static double taxAmount =0;
    static double total = 0;

    static ArrayList<String> strArr = new ArrayList<String>();
    static ArrayAdapter<String> adapter;

    static boolean accountLookupBool = false;


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
                final Button accountLookupButton = (Button) rootView.findViewById(R.id.bAccountLookup);
                final EditText etAccountPhone = (EditText) rootView.findViewById(R.id.etPhoneLookup);
                final TextView tvName = (TextView) rootView.findViewById(R.id.posName);
                final TextView tvNumber = (TextView) rootView.findViewById(R.id.posNumber);
                final TextView tvEmail = (TextView) rootView.findViewById(R.id.posEmail);

                accountLookupButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if(!accountLookupBool) {
                            etAccountPhone.setVisibility(View.VISIBLE);
                            accountLookupButton.setText("Search");
                            accountLookupBool = true;
                        }else{

                            String phone = etAccountPhone.getText().toString();

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Customer");
                            query.whereEqualTo("Phone", phone);
                            query.findInBackground(new FindCallback<ParseObject>() {
                                public void done(List<ParseObject> userList, ParseException e) {
                                    if (e == null) {
                                        tvName.setText("Name: " + userList.get(0).getString("Name"));
                                        tvNumber.setText("Number: " +userList.get(0).getString("Phone"));
                                        tvEmail.setText("Email: " +userList.get(0).getString("email"));
                                    } else {
                                        Log.d("score", "Error: " + e.getMessage());
                                    }
                                }
                            });


                            etAccountPhone.setVisibility(View.GONE);
                            accountLookupButton.setText("Account Lookup");
                            accountLookupBool = false;
                        }

                    }


                });

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
                        customer.put("Phone", etPhoneNumber.getText().toString());
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

                        inventory.put("UPC", etSerialNumber.getText().toString());
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

                final TextView tvSubtotal = (TextView) rootView.findViewById(R.id.posSubtotal);
                final TextView tvTax = (TextView) rootView.findViewById(R.id.posTax);
                final TextView tvTotal = (TextView) rootView.findViewById(R.id.posTotal);

                adapter = new ArrayAdapter<String>( ItemListActivity.mContext.getApplicationContext(), R.layout.customlist,strArr);

                if (strArr.isEmpty())
                 listView = (ListView)rootView.findViewById(R.id.itemLog);

                listView.setAdapter(adapter);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Inventory");
                query.whereEqualTo("UPC", serialNumber);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> itemList, ParseException e) {
                        if (e == null) {
                            String itemName = itemList.get(0).getString("Product_Name");
                            double price = itemList.get(0).getDouble("Price");
                            price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            //update global price variable and labels
                            subtotal += price;
                            taxAmount = subtotal * taxRate;
                            taxAmount = new BigDecimal(taxAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            total = subtotal + taxAmount;
                            tvSubtotal.setText("Subtotal: $" + String.valueOf(subtotal));
                            tvTax.setText("Tax(6.15%): $" + String.valueOf(taxAmount));
                            tvTotal.setText("Total: $" + String.valueOf(total));
                            //update ListView
                            String priceString = String.valueOf(price);
                            strArr.add("  " + itemName + "                                                                                           $" + priceString);
                            adapter.notifyDataSetChanged();
                            System.out.println(strArr.size());
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
