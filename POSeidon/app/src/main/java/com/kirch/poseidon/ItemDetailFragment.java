package com.kirch.poseidon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kirch.poseidon.dummy.DummyContent;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


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

        View rootView;

        switch (mItem.id) {
            case "pos":
                rootView = inflater.inflate(R.layout.activity_pos, container, false);
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
                        ParseUser user = new ParseUser();
                        user.setUsername(etName.getText().toString());
                        user.setEmail(etEmail.getText().toString());
                        user.setPassword("password");
                        user.put("phone", etPhoneNumber.getText().toString());
                        user.put("notes", etNotes.getText().toString());

                        user.signUpInBackground(new SignUpCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    System.out.println("Success");
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
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
                rootView = inflater.inflate(R.layout.add_inventory, container, false);
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
}
