<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;

class AuthController extends Controller
{
    //

    public function login(Request $request)
    {
        try {
            $fields = $request->validate([
                'name' => 'required|string',
                'password' => 'required|string'
            ]);
        } catch (\Illuminate\Validation\ValidationException $e) {

            return response()->json([
                'message' => $e->getMessage()
            ], 400);
        }

        $user = User::where('name', $fields['name'])->first();
        if (!$user || !Hash::check($fields['password'], $user->password)) {
            return response()->json([
                'message' => "bad cred"
            ], 400);
        }

        $user->is_online = 1;
        $user->save();
        $token = $user->createToken('myapptoken')->plainTextToken;
        $response = [
            'id' => $user->id,
            'name' => $user->name,
            'is_online' => $user->is_online,
            'token' => $token
        ];
        return response($response, 201);
    }

    public function registr(Request $request)
    {
        try {
            $fields = $request->validate([
                'name' => 'required|string|unique:users,name',
                'password' => 'required|string|confirmed'
            ]);
        } catch (\Illuminate\Validation\ValidationException $e) {

            return response()->json([
                'message' => $e->getMessage()
            ], 400);
        }

        $user = User::create([
            'name' => $fields['name'],
            'password' => bcrypt($fields['password'])
        ]);
        $token = $user->createToken('myapptoken')->plainTextToken;
        $response = [
            'id' => $user->id,
            'name' => $user->name,
            'is_online' => $user->is_online,
            'token' => $token
        ];
        return response($response, 201);
    }

    public function logout(Request $request)
    {
        $user = User::find($request->input("id"));
        $user->is_online = 0;
        $user->tokens()->delete();
        return response([
            'message' => 'logged out'
        ], 201);
    }
    public function status(Request $request)
    {
        $user = User::where('name', $request->input('name'))->first();
        $user->is_online = $request->input('is_online');
        $user->save();
        return response([
            'message' => 'changed'
        ], 201);
    }
    public function get_user(Request $request)
    {
        $user = User::find($request->input('id'));
        $response = [
            'id' => $user->id,
            'name' => $user->name,
            'is_online' => $user->is_online,
            'token' => $request->input('token')
        ];
        return response($response, 201, []);
    }
}
