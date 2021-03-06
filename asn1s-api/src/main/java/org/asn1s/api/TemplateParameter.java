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

package org.asn1s.api;

import org.asn1s.api.type.Type;
import org.asn1s.api.type.TypeNameRef;
import org.asn1s.api.util.RefUtils;
import org.asn1s.api.value.ValueNameRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TemplateParameter implements Comparable<TemplateParameter>
{
	public TemplateParameter( int index, @NotNull Ref<?> reference, @Nullable Ref<Type> governor )
	{
		this.index = index;
		this.reference = reference;
		this.governor = governor;
	}

	private final int index;
	private final Ref<?> reference;
	private final Ref<Type> governor;

	public int getIndex()
	{
		return index;
	}

	public String getName()
	{
		if( reference instanceof TypeNameRef )
			return ( (TypeNameRef)reference ).getName();

		if( reference instanceof ValueNameRef )
			return ( (ValueNameRef)reference ).getName();

		throw new IllegalStateException();
	}

	@SuppressWarnings( "unchecked" )
	public <T> Ref<T> getReference()
	{
		return (Ref<T>)reference;
	}

	@Nullable
	public Ref<Type> getGovernor()
	{
		return governor;
	}

	public boolean isTypeRef()
	{
		return RefUtils.isTypeRef( reference );
	}

	public boolean isValueRef()
	{
		return RefUtils.isValueRef( reference );
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public boolean equals( Object obj )
	{
		if( this == obj ) return true;
		if( !( obj instanceof TemplateParameter ) ) return false;

		TemplateParameter parameter = (TemplateParameter)obj;

		return getIndex() == parameter.getIndex();
	}

	@Override
	public int hashCode()
	{
		return getIndex();
	}

	@Override
	public String toString()
	{
		if( getGovernor() != null )
			return getGovernor() + ": " + reference;
		return String.valueOf( reference );
	}

	@Override
	public int compareTo( @NotNull TemplateParameter o )
	{
		return Integer.compare( getIndex(), o.getIndex() );
	}
}
