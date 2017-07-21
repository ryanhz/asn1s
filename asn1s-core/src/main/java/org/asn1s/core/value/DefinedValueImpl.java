////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010-2017. Lapinin "lastrix" Sergey.                          /
//                                                                             /
// Permission is hereby granted, free of charge, to any person                 /
// obtaining a copy of this software and associated documentation              /
// files (the "Software"), to deal in the Software without                     /
// restriction, including without limitation the rights to use,                /
// copy, modify, merge, publish, distribute, sublicense, and/or                /
// sell copies of the Software, and to permit persons to whom the              /
// Software is furnished to do so, subject to the following                    /
// conditions:                                                                 /
//                                                                             /
// The above copyright notice and this permission notice shall be              /
// included in all copies or substantial portions of the Software.             /
//                                                                             /
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,             /
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES             /
// OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND                    /
// NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT                /
// HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,                /
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING                /
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE                  /
// OR OTHER DEALINGS IN THE SOFTWARE.                                          /
////////////////////////////////////////////////////////////////////////////////

package org.asn1s.core.value;

import org.asn1s.api.Ref;
import org.asn1s.api.Scope;
import org.asn1s.api.exception.ResolutionException;
import org.asn1s.api.exception.ValidationException;
import org.asn1s.api.module.Module;
import org.asn1s.api.type.DefinedType;
import org.asn1s.api.type.Type;
import org.asn1s.api.value.AbstractDefinedValue;
import org.asn1s.api.value.Value;
import org.jetbrains.annotations.NotNull;

public class DefinedValueImpl extends AbstractDefinedValue
{
	public DefinedValueImpl( @NotNull Module module, @NotNull String name, @NotNull Ref<Type> typeRef, @NotNull Ref<Value> valueRef )
	{
		super( module, name );
		this.typeRef = typeRef;
		this.valueRef = valueRef;
	}

	private Ref<Type> typeRef;
	private Ref<Value> valueRef;

	private Type type;
	private Value value;

	@NotNull
	@Override
	public Scope getScope( @NotNull Scope parentScope )
	{
		return parentScope.typedScope( type );
	}


	Ref<Value> getValueRef()
	{
		return valueRef;
	}

	Ref<Type> getTypeRef()
	{
		return typeRef;
	}

	@Override
	public Type getType()
	{
		return type;
	}

	void setType( Type type )
	{
		this.type = type;
	}

	@Override
	public Value getValue()
	{
		return value;
	}

	void setValue( Value value )
	{
		this.value = value;
	}


	@NotNull
	@Override
	public Kind getKind()
	{
		if( value == null )
			return Kind.Null;

		return value.getKind();
	}

	@Override
	protected void onDispose()
	{
		typeRef = null;
		valueRef = null;
		if( type != null && !( type instanceof DefinedType ) )
			type.dispose();
		type = null;
		value = null;
	}

	@Override
	protected void onValidate( @NotNull Scope scope ) throws ValidationException, ResolutionException
	{
		scope = getModule().createScope();
		type = typeRef.resolve( scope );
		type.validate( scope );
		scope = getScope( scope );
		value = type.optimize( scope, valueRef.resolve( scope ) );
	}

	@Override
	public boolean equals( Object obj )
	{
		if( this == obj ) return true;
		if( !( obj instanceof DefinedValueImpl ) ) return false;

		DefinedValueImpl definedValue = (DefinedValueImpl)obj;

		if( !getName().equals( definedValue.getName() ) ) return false;
		//noinspection SimplifiableIfStatement
		if( !getTypeRef().equals( definedValue.getTypeRef() ) ) return false;
		return getValueRef().equals( definedValue.getValueRef() );
	}

	@Override
	public int hashCode()
	{
		int result = getName().hashCode();
		result = 31 * result + getTypeRef().hashCode();
		result = 31 * result + getValueRef().hashCode();
		return result;
	}

	@Override
	public int compareTo( @NotNull Value o )
	{
		assert isValidated();
		//noinspection CompareToUsesNonFinalVariable
		return value.compareTo( o );
	}


	@Override
	public String toString()
	{
		return getName() + " ::= " + value;
	}
}