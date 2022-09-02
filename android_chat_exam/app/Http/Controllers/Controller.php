<?php

namespace App\Http\Controllers;

use App\Models\Message;
use App\Models\User;
use Illuminate\Foundation\Auth\Access\AuthorizesRequests;
use Illuminate\Foundation\Bus\DispatchesJobs;
use Illuminate\Foundation\Validation\ValidatesRequests;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Illuminate\Routing\Controller as BaseController;

class Controller extends BaseController
{
    use AuthorizesRequests, DispatchesJobs, ValidatesRequests;

    public function get_messages(Request $request)
    {
        $messages = Message::where('id', '>', $request->input('start_id', 0))->get()->toJson();

        return response()->json(['messages' => $messages], 200, []);
    }
    public function send_message(Request $request)
    {
        $message = new Message;
        $message->name = $request->input('name', '');
        $message->message = $request->input('message', '');
        $message->save();
        return response()->json(['detail' => 'success'], 201, []);;
    }
    public function online_users(){
        return response()->json(['users'=>User::orderBy('is_online', 'desc')->get()],201,[]);
    }
    public function set_online(Request $request){
        $user = User::find($request->input('user_id'));
        $user->is_online = $request->input("is_online");
        $user->save();
        return response()->json(['detail'=>'changed'],201,[]);
    }
}
